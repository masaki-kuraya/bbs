package name.kuraya.masaki.bbs.ui.controller;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.server.WebSession;

import name.kuraya.masaki.bbs.ui.model.input.InputPost;
import name.kuraya.masaki.bbs.ui.model.input.Signin;
import name.kuraya.masaki.bbs.ui.model.input.Signup;
import name.kuraya.masaki.bbs.ui.model.output.Pagination;
import name.kuraya.masaki.bbs.ui.model.output.UserInfo;
import name.kuraya.masaki.bbs.ui.service.AccountService;
import name.kuraya.masaki.bbs.ui.service.BbsService;
import name.kuraya.masaki.bbs.ui.service.ServiceException;
import reactor.core.publisher.Mono;

@Controller
public class BbsController {

    private final MessageSource messageSource;

    private final AccountService accountService;
    private final BbsService bbsService;

    public BbsController(MessageSource messageSource, AccountService accountService, BbsService bbsService) {
        this.messageSource = messageSource;
        this.accountService = accountService;
        this.bbsService = bbsService;
    }

    @GetMapping("/")
    public Mono<String> getIndex(
            Model model,
            @RequestParam(name = "p", defaultValue = "1") int page,
            @SessionAttribute UserInfo userInfo,
            Locale locale) {
        final int maxView = 20;
        return bbsService.findOfRange(userInfo.getToken(), maxView, page).map(p -> {
            Pagination pagination = new Pagination(Math.max((p.getTotal() + maxView - 1) / maxView, 1));
            pagination.setCurrent(page);
            model.addAttribute("post", new InputPost(userInfo.getUser().getId(), null));
            model.addAttribute("posts", p.getPosts());
            model.addAttribute("pagination", pagination);
            model.addAttribute("fmtSec", messageSource.getMessage("name.kuraya.masaki.bbs.ui.model.output.OutputPost.fmtSec", null, locale));
            model.addAttribute("fmtMin", messageSource.getMessage("name.kuraya.masaki.bbs.ui.model.output.OutputPost.fmtMin", null, locale));
            model.addAttribute("fmtHour", messageSource.getMessage("name.kuraya.masaki.bbs.ui.model.output.OutputPost.fmtHour", null, locale));
            model.addAttribute("fmtDay", messageSource.getMessage("name.kuraya.masaki.bbs.ui.model.output.OutputPost.fmtDay", null, locale));
            model.addAttribute("fmtMonth", messageSource.getMessage("name.kuraya.masaki.bbs.ui.model.output.OutputPost.fmtMonth", null, locale));
            model.addAttribute("fmt", messageSource.getMessage("name.kuraya.masaki.bbs.ui.model.output.OutputPost.fmt", null, locale));
            return "index";
        });
    }

    @PostMapping("/")
    public Mono<String> postIndex(
            @Validated @ModelAttribute InputPost post,
            BindingResult result,
            @SessionAttribute UserInfo userInfo) {
        return bbsService.post(userInfo.getToken(), post).map(p -> {
            return "redirect:/";
        }).onErrorResume(ex -> ex instanceof ServiceException, ex -> {
            result.rejectValue("comment", "index.validation.comment");
            return Mono.just("redirect:/");
        });
    }

    @GetMapping("/signin.html")
    public String getSignin(Model model) {
        model.addAttribute("signin", new Signin());
        return "signin";
    }

    @PostMapping("/signin.html")
    public Mono<String> postSignin(
            @Validated @ModelAttribute Signin signin,
            BindingResult result,
            WebSession session) {
        if (result.hasErrors()) {
            result.reject("signin.message.error");
            return Mono.just("signin");
        }
        return accountService.signin(signin.getEmail(), signin.getPassword()).map(a -> {
            session.changeSessionId();
            session.getAttributes().put("userInfo", a);
            return "redirect:/";
        }).onErrorResume(ex -> ex instanceof ServiceException, ex -> {
            result.reject("signin.message.error");
            return Mono.just("signin");
        });
    }

    @GetMapping("/signout.html")
    public String getSignout(WebSession session) {
        session.getAttributes().remove("userInfo");
        return "redirect:/signin.html";
    }

    @GetMapping("/signup.html")
    public String getSignup(Model model) {
        model.addAttribute("signin", new Signup());
        return "signup";
    }

    @PostMapping("/signup.html")
    public Mono<String> postSignup(
            Model model,
            @Validated @ModelAttribute Signup signup,
            BindingResult result) {
        if (result.hasErrors()) {
            return Mono.just("signup");
        }
        return accountService.signup(signup).map(v -> {
            return "redirect:/signup-success.html";
        }).onErrorResume(ex -> ex instanceof ServiceException, ex -> {
            ServiceException se = (ServiceException) ex;
            if (se.getError().getErrors().stream().filter(e -> "ALREADY_REGISTERED".equals(e.getCode())).count() > 0) {
                model.addAttribute("signin", new Signup());
                model.addAttribute("registered", true);
                return Mono.just("signup");
            } else {
                throw se;
            }
        });
    }

    @GetMapping("/signup-success.html")
    public String getSignupSuccess(Model model) {
        model.addAttribute("success", true);
        model.addAttribute("signin", new Signin());
        return "signin";
    }

}