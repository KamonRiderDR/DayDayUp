[TOC]

## DFS

### [面试题 04.06. 后继者](https://leetcode-cn.com/problems/successor-lcci/)


设计一个算法，找出二叉搜索树中指定节点的“下一个”节点（也即中序后继）。

如果指定节点没有对应的“下一个”节点，则返回`null`。

------

**简单来说，就是需要返回BST的下一个节点。**

我们需要注意到这样一件事：对于BST，本身的顺序性说明了节点就是比它大的节点中，最小的那一个。

***迭代***相对直观，首先找到该节点，然后再查看其是否具有右子树，如果没有就进行相关操作。

```c++
 TreeNode* inorderSuccessor(TreeNode* root, TreeNode* p) {
        TreeNode* pre = nullptr;
        while( root->val != p->val )
        {
            if( p->val > root->val ) root = root->right;
            else{
                pre = root;
                root = root->left;
            }
        }
        if( root->right == nullptr ) return pre;
        else{
            root = root->right;
            while( root->left != nullptr )
            root = root->left;
        }
        return root;
    }
```

这里需要注意和思考的点是`pre`的更新的时机以及相应的原因。

答：我们这样考虑：***==我们只有当右子树不存在的时候才存在回看的问题。==***

当p节点向右方向移动时，不移动`pre`。因为对于任意一棵子树而言，右子树永远是最后才遍历到的。

------

***递归***要抽象很多，这里先贴出代码：

```c++
class Solution {
public:
    TreeNode* pre = nullptr;	TreeNode* res = nullptr;
    TreeNode* inorderSuccessor(TreeNode* root, TreeNode* p) {
        if( root == nullptr || p == nullptr ) return res;
        inorder( root,p );
        return res;
    }
    void inorder( TreeNode*root,TreeNode*p )
    {
        if( root == nullptr ) return;
        inorder( root->left,p );
        if( pre == p ) res = root;
        pre = root;
        inorder( root->right,p );
    }
};
```

### [106. 从中序与后序遍历序列构造二叉树](https://leetcode-cn.com/problems/construct-binary-tree-from-inorder-and-postorder-traversal/)

```
中序遍历 inorder = [9,3,15,20,7]
后序遍历 postorder = [9,15,7,20,3]

返回如下的二叉树：
	3
   / \
  9  20
    /  \
   15   7
```

------

显然可以递归或迭代。

分析一下$inorder$与$postorder$。
$$
\begin{cases}
inorder:[[left],root,[right]]
\\
postorder:[[left],[right],root]
\end{cases}
$$
换句话说，后序我们获得根，中序我们通过根的index获得左右子树，然后递归。

```c++
    TreeNode* build( int inLeft,int inRight,vector<int>& inorder,vector<int>& postorder )
    {
        if( inLeft>inRight ) return nullptr;
        int rootVal = postorder[ postIndex ];
        
        TreeNode* rootTemp = new TreeNode( rootVal );
        int rootIndex = map[ rootVal ];
        postIndex--;

        //  left,right order is wrong!
        rootTemp->right = build( rootIndex+1,inRight,inorder,postorder );
        rootTemp->left = build( inLeft,rootIndex-1,inorder,postorder );
        return rootTemp;
    }
```

其中map实现了中序的值和位置的映射。注意先左后右会报错。可以这么考虑：由于递归，这里的`postIndex`是全局变量，每次都要减小1（作为寻根），因此必须先右后左边。



下面考虑迭代。

### [124. 二叉树中的最大路径和](https://leetcode-cn.com/problems/binary-tree-maximum-path-sum/)

路径 被定义为一条从树中任意节点出发，沿父节点-子节点连接，达到任意节点的序列。同一个节点在一条路径序列中 至多出现一次 。该路径 至少包含一个 节点，且不一定经过根节点。

路径和 是路径中各节点值的总和。

给你一个二叉树的根节点 root ，返回其 最大路径和 。

```
输入：root = [-10,9,20,null,null,15,7]
输出：42
解释：最优路径是 15 -> 20 -> 7 ，路径和为 15 + 20 + 7 = 42
```

------

首先分析题目，换句话说，我们要找一条没有分岔的路径，最后的和最大。那么必定有一个最高的节点（ 我们记为 root ）。我们可以看成：
$$
root.val + sum(leftTree)+sum(rightTree)
$$
如此一来，递归（DFS）便有了。

<img src=".\image\DFS_01.png" style="zoom:60%;" />

注意对于这里的`*p`，可能是一个中间节点，这意味着我们要将其作为结果返回。即：

（1）对上返回其中一条路径的结果（左or右）

（2）对下本身的完整的一条路也可能是结果，先记录下来。

```c++
        int leftMax = max( DFS( root->left ),0 );
        int rightMax = max( DFS( root->right ),0 );
        maxVal = max( maxVal,root->val+leftMax+rightMax );
        return ( root->val+max( leftMax,rightMax ) );
```

### [面试题34. 二叉树中和为某一值的路径](https://leetcode-cn.com/problems/er-cha-shu-zhong-he-wei-mou-yi-zhi-de-lu-jing-lcof/)

输入一棵二叉树和一个整数，打印出二叉树中节点值的和为输入整数的所有路径。从树的根节点开始往下一直到叶节点所经过的节点形成一条路径。

 ```
			  5
             / \
            4   8
           /   / \
          11  13  4
         /  \    / \
        7    2  5   1

[
   [5,4,11,2],
   [5,8,4,5]
]
 ```

------

注意是从根节点到叶节点。标准的DFS套路( 3*steps )：

```c++
t.push(val) 
DFS()
t.pop()
```

这里只要保存当前的解，以及一个标记是否到头的情况就好了。（如果都是正的，可以剪枝）

```c++
 		//	step 1
		path.push_back( root->val );
        currentSum -= root->val;
		//	step 2
        if( currentSum != 0 || root->left || root->right )
        {
            DFS( root->left,currentSum );
            DFS( root->right,currentSum );
        }
        else res.push_back( path );
		//	step 3
        path.pop_back();
```

### [面试题 17.22. 单词转换](https://leetcode-cn.com/problems/word-transformer-lcci/)

给定字典中的两个词，长度相等。写一个方法，把一个词转换成另一个词， 但是一次只能改变一个字符。每一步得到的新词都必须能在字典中找到。

编写一个程序，返回一个可能的转换序列。如有多个可能的转换序列，你可以返回任何一个。

```
输入:
beginWord = "hit",
endWord = "cog",
wordList = ["hot","dot","dog","lot","log","cog"]

输出:
["hit","hot","dot","lot","log","cog"]
```

------

这里是一个图。对于每一个可能的单词，我们需要遍历整个字典，显然，那些已经出现的词就不考虑了。

老规矩，三步走。注意最后一步。

```c++
 			if( isVisited[i] || !change( currentWord,wordList[i] ) ) continue;
            isVisited[i] = true;
            res.push_back( wordList[i] );
            if( DFS( wordList[i],endWord,wordList ) ) return true;
            res.pop_back();
            //isVisited[i] = false;
```

如果reset，会报超时的错误~



### [1028. 从先序遍历还原二叉树](https://leetcode-cn.com/problems/recover-a-tree-from-preorder-traversal/)

我们从二叉树的根节点 root 开始进行深度优先搜索。

在遍历中的每个节点处，我们输出 D 条短划线（其中 D 是该节点的深度），然后输出该节点的值。（如果节点的深度为 D，则其直接子节点的深度为 D + 1。根节点的深度为 0）。

如果节点只有一个子节点，那么保证该子节点为左子节点。

给出遍历输出 S，还原树并返回其根节点 root。

```
输入："1-2--3--4-5--6--7"
输出：[1,2,5,3,4,6,7]
```

------

我们考虑如何确定每一个节点？显然需要知道值和深度。深度用来插值，值用来构造节点。如下，我们需要双指针定位度数和值。

<img src=".\image\DFS_02.png" style="zoom:67%;" />

```c++
        queue<int> d,v;
```

```c++
for(int i=0;i<S.size();i++){
            if(S[i]!='-'){
                d.push(i-left);
                left = i;
                while(i<S.size() && S[i]!='-') i++;
                v.push(stoi(S.substr(left,i-left)));
                left = i;
            }
        }
```

我们可以采用递归来做。

```c++
 TreeNode* build(queue<int>& d,queue<int>& v,int depth){
        if(d.front()!=depth) return NULL;
        TreeNode* root = new TreeNode(v.front());
        d.pop();v.pop();
        root->left = build(d,v,depth+1);
        root->right = build(d,v,depth+1);
        return root;
 }
```

***Notice:***

（1）为什么左右操作是紧挨着的？不要自底向上考虑，要自顶向下考虑，交给子节点去做就好！

---

<div align = 'center'><font color = red><B>TOO BAD!

How to improve? **迭代**

栈模拟！

```c++
TreeNode* recoverFromPreorder(string S) {
        int position = 0;
        stack< TreeNode* > path;
        while( position != S.size() )
        {
            //  get current value
            int depth = 0;
            while( S[position] == '-' )
            {
                depth++;
                position++;
            }
            //  get new value
            int value = 0;
            while( position != S.size() && isdigit( S[position] ) )
            {
                value = value*10 + (S[position]-'0');
                position++;
            }

            TreeNode* root = new TreeNode( value );
            //  get root->left and root->right
            if( depth == path.size() )
            {
                if( !path.empty() ) path.top()->left = root;
            }
            else
            {
                while( path.size() != depth ) path.pop();
                path.top()->right = root;
            }
            path.push( root );
        }

        while( path.size() > 1 ) path.pop();
        return path.top();
    }
```



### [99. 恢复二叉搜索树](https://leetcode-cn.com/problems/recover-binary-search-tree/)

给你二叉搜索树的根节点 root ，该树中的两个节点被错误地交换。请在不改变其结构的情况下，恢复这棵树。

进阶：使用 O(n) 空间复杂度的解法很容易实现。你能想出一个只使用常数空间的解决方案吗？

------



题目指明了是有两个节点交换。首先必然考虑如果只是两个节点被交换产生的影响。我们任意地调换一棵BST中两个节点的位置。比较直接的想法是中序遍历，这样就会发现两个大小关系颠倒的节点，然后交换就好。

<img src=".\image\DFS_03.png" style="zoom: 33%;" />



显然性能糟糕。考虑进一步优化。我们每次比较都是比较的相邻的节点的大小关系，因此我们将问题简化为模拟中序遍历，每次仅考虑当前节点与后面的一个节点。

```c++
 while( root || !stack.empty() )
        {
            while( root )
            {
                stack.push( root );
                root = root->left;
            }
     		//	core code
            root = stack.top();
            stack.pop();
            if( pred && pred->val>root->val )
            {
                y = root;
                if( !x ) x = pred;
                else break;
            }
     	    //	end core code
     
            pred = root;
            root = root->right;
        }
```

官方给出的两种解法都是对于求先序中的所谓“反常点”。当然还有另一种算法，不过大体上来说是对内存的优化，核心思想并无差别。



### [面试题 04.12. 求和路径](https://leetcode-cn.com/problems/paths-with-sum-lcci/)

给定一棵二叉树，其中每个节点都含有一个整数数值(该值或正或负)。设计一个算法，打印节点数值总和等于某个给定值的所有路径的数量。注意，路径不一定非得从二叉树的根节点或叶节点开始或结束，但是其方向必须向下(只能从父节点指向子节点方向)。

```
 			  5
             / \
            4   8
           /   / \
          11  13  4
         /  \    / \
        7    2  5   1
-------------------------------------------------------
3
解释：和为 22 的路径有：[5,4,11,2], [5,8,4,5], [4,11,7]

```









































------

## BFS

### [909. 蛇梯棋](https://leetcode-cn.com/problems/snakes-and-ladders/)

r 行 c 列的棋盘，按前述方法编号，棋盘格中可能存在 “蛇” 或 “梯子”；如果 board[r][c] != -1，那个蛇或梯子的目的地将会是 `board[r][c]`。

玩家从棋盘上的方格 1 （总是在最后一行、第一列）开始出发。

每一回合，玩家需要从当前方格 x 开始出发，按下述要求前进：

选定目标方格：选择从编号 x+1，x+2，x+3，x+4，x+5，或者 x+6 的方格中选出一个目标方格 s ，目标方格的编号 <= N*N。
该选择模拟了掷骰子的情景，无论棋盘大小如何，你的目的地范围也只能处于区间 [x+1, x+6] 之间。
传送玩家：如果目标方格 S 处存在蛇或梯子，那么玩家会传送到蛇或梯子的目的地。否则，玩家传送到目标方格 S。 
注意，玩家在每回合的前进过程中最多只能爬过蛇或梯子一次：就算目的地是另一条蛇或梯子的起点，你也不会继续移动。

返回达到方格 N*N 所需的最少移动次数，如果不可能，则返回 -1。

<img src="image/snakes.png" style="zoom:50%;" />

---

```
输入：[
[-1,-1,-1,-1,-1,-1],
[-1,-1,-1,-1,-1,-1],
[-1,-1,-1,-1,-1,-1],
[-1,35,-1,-1,13,-1],
[-1,-1,-1,-1,-1,-1],
[-1,15,-1,-1,-1,-1]]
输出：4
解释：
首先，从方格 1 [第 5 行，第 0 列] 开始。
你决定移动到方格 2，并必须爬过梯子移动到到方格 15。
然后你决定移动到方格 17 [第 3 行，第 5 列]，必须爬过蛇到方格 13。
然后你决定移动到方格 14，且必须通过梯子移动到方格 35。
然后你决定移动到方格 36, 游戏结束。
可以证明你需要至少 4 次移动才能到达第 N*N 个方格，所以答案是 4。
```

---

分析一下寻路的过程，可以迅速发现这是一个BFS问题。我们对于当前的结点$p$，需要判断向后六个点。从这可以看出，这跟整个二维数组是没有什么关系的。事实上，**我们完全可以用一维数组搞定**。进一步地，这就是一个图的连接，结点总共有<font color = red>$ n^2$</font>。

当然，我们必须注意二维和一维位置之间的转化。

```c++
public:
    //  id => ( row,col )
    pair<int,int> id2rc( int id,int n )
    {
        int row = ( id-1 )/n;
        int col = ( id-1 )%n;
        //  考虑迂回
        if( row%2 == 1 ) col = n-1-col;

        return { n-1-row,col };
    }
```

具体的细节在代码后讨论。

```c++
   int snakesAndLadders(vector<vector<int>>& board) {
        int  n = board.size();
        queue< pair<int,int> > queue;   //  queue( id,times )
        queue.emplace( 1,0 );
        vector< bool > isVisited( n*n+1 );

        while( !queue.empty() )
        {
            auto p = queue.front();
            queue.pop();

            //  forwarding *p
            for( int i = 1;i <= 6;i++ )
            {
                //  get next location
                int pnext = p.first + i;
                if( pnext>n*n ) break;

                auto location = id2rc( pnext,n );
                int rowLoc = location.first;
                int colLoc = location.second;
                if( board[rowLoc][colLoc] > 0 )
                {
                    //  has a ladder => jump!
                    pnext = board[rowLoc][colLoc];
                }
                if( pnext == n*n ) return p.second+1;

                //  else we continue
                if( !isVisited[pnext] )
                {
                    isVisited[pnext] = true;
                    queue.emplace( pnext,p.second+1 );
                }
            }
        }
        return -1;
    }
```

有两个细节需要注意。

1. **判断条件的顺序**：先判断这个点合不合适再插入。
2. **为啥直接返回就一定是最小（不需要比较的🏇？）**因为我们是广度优先遍历。换句话说，在同一次`for`循环中，大家的地位是相同的。因此我如果此时返回，必然是最小的。

### [815. 公交路线](https://leetcode-cn.com/problems/bus-routes/)

给你一个数组 routes ，表示一系列公交线路，其中每个 routes[i] 表示一条公交线路，第 i 辆公交车将会在上面循环行驶。

例如，路线 routes[0] = [1, 5, 7] 表示第 0 辆公交车会一直按序列 1 -> 5 -> 7 -> 1 -> 5 -> 7 -> 1 -> ... 这样的车站路线行驶。
现在从 source 车站出发（初始时不在公交车上），要前往 target 车站。 期间仅可乘坐公交车。

求出 最少乘坐的公交车数量 。如果不可能到达终点车站，返回 -1 。

---

```
输入：routes = [[1,2,7],[3,6,7]], source = 1, target = 6
输出：2
解释：最优策略是先乘坐第一辆公交车到达车站 7 , 然后换乘第二辆公交车到车站 6 。 
```

---

注意我们不一定非要走到头，可能就已经知道两个**公交车**之间是可达的了。

- 我们可以从输入数据入手，构造出一个***图***。

```c++
        vector< vector<int> > graph( n,vector<int>(n) );    //  graph( station,station )
        unordered_map< int,vector<int> > map;   //  map( index,bus-routes )
        for( int i = 0 ;i != n;i++ )
        {
            for( auto& site:routes[i] )
            {
                //  site <=> i
                for( auto& j:map[site] )
                {
                    graph[i][j] = true;
                    graph[j][i] = true;
                }

                map[site].push_back( i );
            }
        }
```

注意这里构造图的过程。事实上，我们对整个`routers`进行遍历，也可以得到这样的结果。这里的map`push`的方式是不能调换的，因为这是一个<font color = red>有向图</font>。

- 然后就是标准的BFS问题了。

```c++
     //  BFS
        queue< int > queue;
        vector< int > distance( n,-1 );
        for( auto& bus:map[source] )
        {
            distance[bus] = 1;
            queue.push( bus );
        }

        while( !queue.empty() )
        {
            int i = queue.front();
            queue.pop();
            for( int j = 0;j != n;j++ )
            {
                if( graph[i][j] == true && distance[j] == -1 )
                {
                    distance[j] = distance[i] + 1;
                    queue.push( j );
                }
            }
        }

        int res = INT_MAX;
        for( auto& bus:map[target] )
        {
            if( distance[bus] != -1 ) res = min( res,distance[bus] );
        }
        return res == INT_MAX ? -1 : res;
```

这里依然有一个小细节：`distance[j] == -1`这句话的用处。其实和之前一样，当你使用BFS的时候，你本身就已经<font color = red>**隐含了层次的信息**</font>。换言之，当我们更新`distance[j]`的时候，如果其值不为-1，则这个解必定比之后更新的值要优。

### [剑指 Offer 37. 序列化二叉树](https://leetcode-cn.com/problems/xu-lie-hua-er-cha-shu-lcof/)

请实现两个函数，分别用来序列化和反序列化二叉树。

你需要设计一个算法来实现二叉树的序列化与反序列化。这里不限定你的序列 / 反序列化算法执行逻辑，你只需要保证一个二叉树可以被序列化为一个字符串并且将这个字符串反序列化为原始的树结构。

提示：输入输出格式与 LeetCode 目前使用的方式一致，详情请参阅 LeetCode 序列化二叉树的格式。你并非必须采取这种方式，你也可以采用其他的方法解决这个问题。

---

所谓序列化，就是指树的层次遍历。反序列化，相对应地就是指由先序遍历恢复树的结构。

可以采用类似BFS的思路，我们使用`queue`。这样，每当在同一层遍历的时候，我们都可以按顺序添加进去。

**代码略。**

### [LCP 07. 传递信息](https://leetcode-cn.com/problems/chuan-di-xin-xi/)

小朋友 A 在和 ta 的小伙伴们玩传信息游戏，游戏规则如下：

有 n 名玩家，所有玩家编号分别为 0 ～ n-1，其中小朋友 A 的编号为 0
每个玩家都有固定的若干个可传信息的其他玩家（也可能没有）。传信息的关系是单向的（比如 A 可以向 B 传信息，但 B 不能向 A 传信息）。
每轮信息必须需要传递给另一个人，且信息可重复经过同一个人
给定总玩家数 n，以及按 [玩家编号,对应可传递玩家编号] 关系组成的二维数组 relation。返回信息从小 A (编号 0 ) 经过 k 轮传递到编号为 n-1 的小伙伴处的方案数；若不能到达，返回 0。

---

```
输入：n = 5, relation = [[0,2],[2,1],[3,4],[2,3],[1,4],[2,0],[0,4]], k = 3**

**输出：3**

**解释：信息从小 A 编号 0 处开始，经 3 轮传递，到达编号 4。共有 3 种方案，分别是 0->2->0->4， 0->2->1->4， 0->2->3->4。
```

---

图相关的遍历问题。采用BFS去做。大致分为三步：

- 构造图，由输入构造点到点之间的有向边。
- BFS。基本思想一致，稍微有些不同的地方就是我们需要每次把同一层的所有元素都给`push`出去，因为我们最后是希望拿到同一层的所有结果的。代码如下：

```c++
        int step = 0;
        queue< int > queue;
        queue.push(0);
        while( !queue.empty() && step<k )
        {
            //  BFS
            int n = queue.size();
            for( int i = 0;i !=n;i++ )
            {
                int t = queue.front();
                queue.pop();
                for( auto& nxt:edges[t] ) queue.push( nxt );
            }
            step++;
        }
```

- 结果的展示。	

由于这是计数问题，因此我们也可以使用动态规划解决这道题。我们记录每一次到达相关节点的路径的路径总数：$dp[i+1][j] = \sum_k dp[i][k](edge[k][j] \not = 0)$

```c++
        vector<vector<int>> dp( k+1,vector<int>(n) );
        dp[0][0] = 1;
        for( int i = 0;i != k;i++ )
        {
            for( auto& edge:relation )
            {
                dp[ i+1 ][edge[1]] += dp[i][edge[0]];
            }
        }
        return dp[k][n-1];
```

## Topological Sort

### [207. 课程表](https://leetcode-cn.com/problems/course-schedule/) 

你这个学期必须选修 numCourses 门课程，记为 0 到 numCourses - 1 。

在选修某些课程之前需要一些先修课程。 先修课程按数组 prerequisites 给出，其中 prerequisites[i] = [ai, bi] ，表示如果要学习课程 ai 则 必须 先学习课程  bi 。

例如，先修课程对 [0, 1] 表示：想要学习课程 0 ，你需要先完成课程 1 。
请你判断是否可能完成所有课程的学习？如果可以，返回 true ；否则，返回 false 。

```
输入：numCourses = 2, prerequisites = [[1,0]]
输出：true
解释：总共有 2 门课程。学习课程 1 之前，你需要完成课程 0 。这是可能的。
```

---

经典的拓扑排序，我们用两种方法都来做一下（DFS && BFS）。注意这里原图的边的顺序是反的，也就是我们需要重新构造新的图。

`BFS` 也就是所谓的官方拓扑排序，我们用栈来实现（`queue`同样OK）

```c++
class Solution {
    vector<int> vertexIn;   //  入度
    vector< vector<int> > edges;	// reconstruct the graph
public:
    bool canFinish(int numCourses, vector<vector<int>>& prerequisites) {
        //  init
        edges.resize( numCourses );
        vertexIn.resize( numCourses );
        for( auto& ele:prerequisites )
        {
            edges[ ele[1] ].push_back( ele[0] );
            vertexIn[ele[0]]++;
        }
        // topological sort
        stack<int> stack;
        for( int i = 0;i != vertexIn.size();i++ )
            if( vertexIn[i] == 0 ) stack.push( i );
        int count = 0;
        while( !stack.empty() )
        {
            count++;
            int val = stack.top();
            stack.pop();
            for( auto& v:edges[val] )
            {
                vertexIn[v]--;
                if( vertexIn[v] == 0 ) stack.push(v);
            }
        }
		//	end
        return count == numCourses;
    }
};
```

`DFS`：首先要明确，递归隐式地维护了一个栈。而在BFS中，弹出元素后压入其后继顶点替代。

但是，这里并不需要打印最终的结果，因此，只需要作出判断即可。

```c++
    void DFS( int v )
    {
        visited[v] = 1;
        for( auto& e:edges[v] )
        {
            //  not visited
            if( visited[e] == 0 )
            {
                DFS(e);
                if( !valid ) return;
            }
            //  remain unchanged
            else if( visited[e] == 1 )
            {
                valid = false;
                return;
            }
            //  else, already in the stack
        }
        visited[v] = -1;
    }
```

主函数中，对每个顶点调用一次：

```c++
				for(auto& edge:edges)	DFS(edge);
```

总共是三种情况：其中第二种是有环的情况。**换个角度**，从解集的角度考虑：解集是树的形式，也就是说，我们的这个值始终是没有变化的，意味着出现环了。

### [210. 课程表 II](https://leetcode-cn.com/problems/course-schedule-ii/)

题干和207一样。

```
输入: 2, [[1,0]] 
输出: [0,1]
解释: 总共有 2 门课程。要学习课程 1，你需要先完成课程 0。因此，正确的课程顺序为 [0,1] 。
```

---

这里我们需要输出结果，因此需要考虑整个输出的顺序。我们依然考虑两种解法。

`BFS`：代码逻辑不变，只不过需要中途打印结果最后判断的标准是：$res.size() == numCourse.size()$。

`DFS`：其实也很类似，我们只需要在每次`DFS`的时候保存该节点就好。

我们因为使用了stl就不需要像书上一样构造逆邻接表。

