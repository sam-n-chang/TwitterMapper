package filters;


import twitter4j.Status;

import java.util.List;

/**
 * A filter that represents the logical not of its child filter
 */
public class OrFilter implements Filter {
    private final Filter lchild;
    private final Filter rchild;

    public OrFilter(Filter lchild, Filter rchild) {
        this.lchild = lchild;
        this.rchild = rchild;
    }

    /**
     * A not filter matches when its child doesn't, and vice versa
     * @param s     the tweet to check
     * @return      whether or not it matches
     */
    @Override
    public boolean matches(Status s) {
        return (lchild.matches(s) || rchild.matches(s));
    }

    @Override
    public List<String> terms() {
        List<String> newList = lchild.terms();
        newList.addAll(rchild.terms());
        return newList;
    }

    public String toString() {
        return "("+lchild.toString() + " or " + rchild.toString()+")";
    }
}