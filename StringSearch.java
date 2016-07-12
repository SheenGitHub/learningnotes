import java.util.*;

/**
 * Created by Administrator on 2016/7/12.
 */
public class StringSearch {

    private TreeNode _root;

    public StringSearch(String[] keywords) {
        buildTree(keywords);
        addFailure();
    }
public static final String text ="然而100年后的今天，我们必须正视黑人还没有得到自由这一悲惨的事实。100年后的今天，在种族隔离的镣铐和种族歧视的枷锁下，黑人的生活备受压榨。100年后的今天，黑人仍生活在物质充裕的海洋中一个穷困的孤岛上。100年后的今天，黑人仍然蜷缩在美国社会的角落里，并且意识到自己是故土家园中的流亡者。今天我们在这里集会，就是要把这种骇人听闻的情况公诸世人";
public static final  String[] words = new String[]{"黑人","自由","物质","种族","美国","社会"};
    public static void main(String[] args) {
        StringSearch search = new StringSearch(words);
        StringSearchResult[] results = search.findAll(text);
        System.out.println(Arrays.asList(results));
    }

    private void buildTree(String[] keywords) {
        _root = new TreeNode(null, ' ');
        for (String p : keywords) {
            TreeNode nd = _root;
            for (char c : p.toCharArray()) {
                TreeNode ndNew = null;
                for(TreeNode item : nd.transitions())
                    if(item.getChar() == c){
                        ndNew =item;
                        break;
                    }
                if (ndNew == null) {
                    ndNew = new TreeNode(nd, c);
                    nd.addTransition(ndNew);
                }

                nd = ndNew;
                }
            nd.addResult(p);

            }
        }


    private void addFailure() {
        List<TreeNode> nodes = new ArrayList<>();
        for (TreeNode node : _root.transitions()) {
            node.failure(_root);
            for (TreeNode n : node.transitions())
                nodes.add(n);
        }

        while (nodes.size() > 0) {
            List<TreeNode> newNodes = new ArrayList<>();
            for (TreeNode t : nodes) {
                TreeNode r = t._parent._failure;
                char c = t.getChar();
                while (r!=null && ! r.containsTransition(c))
                    r = r._failure;
                if (r == null)
                    t.failure(_root);
                else{
                    t._failure = r.getTransition(c);
                    r._results.stream().forEach(t::addResult);
                }

                for (TreeNode nd : t.transitions()) {
                    newNodes.add(nd);
                }
            }
            nodes = newNodes;
        }
        _root.failure(_root);
    }

    public void depthSearch(TreeNode node, StringBuilder ret, int depth) {
        if (node != null) {
            for (int i = 0; i < depth; i++) {
                ret.append("| ");
            }
            ret.append("|--");
            ret.append(node._char + "\n");
            for (TreeNode child : node.transitions()) {
                int childDeath = depth + 1;
                depthSearch(child,ret,childDeath);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append("打印树节点: \n");
        int depth = 0;
        depthSearch(_root, ret, depth);
        return ret.toString();
    }

    public StringSearchResult[] findAll(String text) {
        List<StringSearchResult> ret = new ArrayList<>();
        int index = 0;
        TreeNode ptr = _root;

        while (index < text.length()) {
            TreeNode trans = null;
            while (trans == null) {
                trans = ptr.getTransition(text.charAt(index));
                if(ptr == _root)
                    break;
                if(trans == null)
                    ptr = ptr._failure;
            }
            if(trans != null)
                ptr = trans;
//            for(String found: trans.results())
//                ret.add(new StringSearchResult(index - found.length()+1,found));
            final int finalIndex = index;
            ptr.results().stream()
                    .map(t -> new StringSearchResult(finalIndex - t.length()+1,t))
                    .forEach(ret::add);
            index++;

        }
        return ret.toArray(new StringSearchResult[ret.size()]);

    }

    public void visitRoot(Visitor<TreeNode> visitor) {
        visitTree(_root,visitor);
    }

    public static void visitTree(TreeNode root ,Visitor<TreeNode> visitor) {
        if (root != null) {
            List<TreeNode> children = Arrays.asList(root.transitions());
            for (TreeNode item : children) {
                visitor.visit(item);
                visitTree(item,visitor);
            }
        }

    }

    private final class TreeNode {
        private char _char;
        private TreeNode _parent;
        private TreeNode _failure;
        private ArrayList<String> _results;
        private TreeNode[] _transitionsAr;
        private Hashtable<Character,TreeNode> _transHash;

        public TreeNode(TreeNode _parent, char _char) {
            this._char = _char;
            this._parent = _parent;
            this._results = new ArrayList<>();
            this._transitionsAr = new TreeNode[]{};
            this._transHash = new Hashtable<>();
        }

        public void addResult(String result) {
            if(_results.contains(result))
                return;
            _results.add(result);
        }



        public void addTransition(TreeNode node) {
            _transHash.put(node._char, node);
            TreeNode[] ar = new TreeNode[_transHash.size()];
            Iterator<TreeNode> it = _transHash.values().iterator();
            for (int i = 0; i < ar.length; i++) {
                if (it.hasNext()) {
                    ar[i] = it.next();
                }
            }
            _transitionsAr = ar;
        }

        public TreeNode getTransition(char c) {
            return _transHash.get(c);
        }

        public boolean containsTransition(char c) {
            return getTransition(c) != null;
        }

        public char getChar() {
            return _char;
        }

        public TreeNode parent() {
            return _parent;
        }

        public TreeNode failure(TreeNode value) {
            _failure = value;
            return _failure;
        }

        public TreeNode[] transitions() {
            return _transitionsAr;
        }

        public ArrayList<String> results() {
            return _results;
        }
    }

    static class StringSearchResult {
        private String found;
        private int index;

        public StringSearchResult(int index, String found) {
            this.index = index;
            this.found = found;
        }

        public String getFound() {
            return found;
        }

        public int getIndex() {
            return index;
        }

        @Override
        public String toString() {
            return found + "@"+index;
        }
    }
    class NodeVisitor implements Visitor<TreeNode> {

        @Override
        public void visit(TreeNode item) {
            System.out.println(item.getChar());
        }
    }
}

interface Visitor<T> {
    void visit(T item);
}
