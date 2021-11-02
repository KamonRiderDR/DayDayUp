[TOC]

# 树刷题





## Union Set

### [面试题 17.07. 婴儿名字](https://leetcode-cn.com/problems/baby-names-lcci/)

每年，政府都会公布一万个最常见的婴儿名字和它们出现的频率，也就是同名婴儿的数量。有些名字有多种拼法，例如，John 和 Jon 本质上是相同的名字，但被当成了两个名字公布出来。给定两个列表，一个是名字及对应的频率，另一个是本质相同的名字对。设计一个算法打印出每个真实名字的实际频率。注意，如果 John 和 Jon 是相同的，并且 Jon 和 Johnny 相同，则 John 与 Johnny 也相同，即它们有传递和对称性。

在结果列表中，选择 字典序最小 的名字作为真实名字。

```
输入：names = ["John(15)","Jon(12)","Chris(13)","Kris(4)","Christopher(19)"], synonyms = ["(Jon,John)","(John,Johnny)","(Chris,Kris)","(Chris,Christopher)"]
输出：["John(27)","Chris(36)"]
```

**Analysis**

27 = 15+12 while 36 = 13+4+19

Union Set contains names that have the same meaning: $<name1,name2,...,namek>$. 

The root of the name contains the total value.

```java
class Solution {
    public String[] trulyMostPopular(String[] names, String[] synonyms) {
        UnionFind uf = new UnionFind();
        for(String str : names) {
            int index1 = str.indexOf('('), index2 = str.indexOf(')');
            String name = str.substring(0, index1);
            int count = Integer.valueOf(str.substring(index1 + 1, index2));
            //并查集初始化
            uf.parent.put(name, name);
            uf.size.put(name, count);

        }
        for(String synonym : synonyms) {
            int index = synonym.indexOf(',');
            String name1 = synonym.substring(1, index);
            String name2 = synonym.substring(index + 1, synonym.length() - 1);
            //避免漏网之鱼
            if(!uf.parent.containsKey(name1)) {
                uf.parent.put(name1, name1);
                //注意人数为0
                uf.size.put(name1, 0);
            }
            if(!uf.parent.containsKey(name2)) {
                uf.parent.put(name2, name2);
                uf.size.put(name2, 0);
            }
            uf.union(name1, name2);
        } 
        List<String> res = new ArrayList<>();
        for(String str : names) {
            int index1 = str.indexOf('('), index2 = str.indexOf(')');
            String name = str.substring(0, index1);
            //根节点
            if(name.equals(uf.find(name))) 
                res.add(name + "(" + uf.size.get(name) + ")");    
        }
         return res.toArray(new String[res.size()]);
    }
}

//并查集
//路径压缩
public class UnionFind{
    //当前节点的父亲节点
    Map<String, String> parent;
    //当前节点人数
    Map<String, Integer> size;

    public UnionFind() {
        this.parent = new HashMap<>();
        this.size = new HashMap<>();
    }

    //找到x的根节点
    public String find(String x) {
        if(parent.get(x).equals(x))
            return x;
        //路径压缩
        parent.put(x, find(parent.get(x)));
        return parent.get(x);
    }

    public void union(String x, String y) {
        String str1 = find(x), str2 = find(y);
        if(str1.equals(str2))
            return;
        //字典序小的作为根
        if(str1.compareTo(str2) > 0) {
            parent.put(str1, str2);
            //人数累加到根节点
            size.put(str2, size.get(str1) + size.get(str2));
        }else {
            parent.put(str2, str1);
            size.put(str1, size.get(str2) + size.get(str1));
        }
    }
    //以x为根结点的子树包含的人数
    public int getSize(String x) {
        String str = find(x);
        return size.get(str);
    }
}
```

