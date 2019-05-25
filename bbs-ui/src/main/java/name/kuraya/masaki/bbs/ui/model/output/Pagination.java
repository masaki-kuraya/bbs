package name.kuraya.masaki.bbs.ui.model.output;

import java.util.ArrayList;
import java.util.List;

public class Pagination {

    private final int total;

    private int current = 1;
    private int limit = 5;
    private int offset = 2;

    public Pagination(int total) {
        this.total = total;
    }

    public boolean isFirst() {
        return current == 1;
    }

    public boolean isLast() {
        return current == total;
    }

    public int getNext() {
        if (current >= total) {
            throw new IllegalStateException("次のページは存在しません。既に最後のページです。");
        }
        return current + 1;
    }

    public int getPrevious() {
        if (current <= 1) {
            throw new IllegalStateException("前のページは存在しません。既に最初のページです。");
        }
        return current - 1;
    }

    public List<Integer> getNumbers() {
        int end = Math.min(Math.max(current - offset, 1) + limit, total + 1);
        int begin = Math.max(end - limit, 1);
        return range(begin, end);
    }

    private static List<Integer> range(int begin, int end) {
        List<Integer> list = new ArrayList<>(end - begin);
        for (int i = begin; i < end; i++) {
            list.add(i);
        }
        return list;
    }

    public int getTotal() {
        return this.total;
    }

    public int getCurrent() {
        return this.current;
    }

    public void setCurrent(int page) {
        if (page < 1) {
            throw new IllegalArgumentException("最初のページより小さいページは指定できません。");
        } else if (page > total) {
            throw new IllegalArgumentException("最大ページ数より大きいページは指定できません。");
        }
        this.current = page;
    }

    public boolean isInvalidCurrent(int page) {
        return page < 1 || page > total;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}