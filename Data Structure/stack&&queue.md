# **Stack && Queue**

[TOC]

$$
\textbf{DR \ \ \ \ \ \ \ \ \ \ 2021/11/19 \ \ \ \ \ \ \ \ \ V1.0}
\\
----------------------
$$



> 用各种数据结构是为了减少存储（空间）与加快查找（速度）。但是栈和队列显然与这些不符（甚至包括链表），更多情况下，我们把它看成是实际情况的一种模拟。说这些是因为在操作系统中两种结构使用的相当频繁。

## Definition

All are **limited** *Linear List*!

## ADT

### Sequential stack

```c++
int data[maxSize];
int top;
```

It is obvious that $top \in [-1,size-1]$.  It means that we need to judge when `push()/pop()`.



### Chain stack

```c++
int data;
LNode *next;
```



### Queue

We need **capacity,front,rear** to describe the structure.

Simple sequential queue may cause **False Overflow**! $\to$ <font color = red>$Circular linked list$</font>



## Infix $\leftrightarrow$ postfix

<img src=".\image\Stack_01.png" style="zoom:50%;" />

In postfix, $A' \leftarrow A/B$, the result is a new variable.

- Infix $\to$ Postfix
- Evaluate postfix

Combined with **COMPILER**, we need to consider the ***priority*** of the operators! (Actually make it in LL(1) Grammar!)

### **Infix to postfix**

To begin with, we add brackets to each calculation.

We just output the **operand** while storing the **operators** in a stack. It is unavoidable to meet the priority problem.

If $isp < icp$, then we ***pop***. Otherwise we just ***push***.
$$
for \ x \ in \ S
\\
if \ x \in operand
\\
print(x)
\\
elif \ x \in opeator
\\
pop() \ until \ isp(s.top()) > icp(x)
\\
push(x)
\\
elif \ x==')'
\\
print \ all \ the \ operators \ in \ the \ bracket \ pair
\\
\ 
\\
\color{blue}(\star)empty \ stack
$$

-----

> （1）我们用`stack`存放操作符，而直接输出字符——stack本身代表了优先级，而对于字符而言是没有优先级的。
>
> （2）关于操作：我们计算的时候读到操作符就直接计算而不需比较，***这就意味着我们***转化为后缀表达式的时候也需要保证**每次输出最高级别的运算符**。而输出又对应着弹栈的操作，因此我们比较，如果`cin`的优先级高就压栈，反之就一直弹栈。
>
> $\Rightarrow$ 同理可以推导出来左括号（$Highest$）和右括号（$Lowest$）的优先级。



### **Postfix calculation**

We just need to use the correct number in the calculation!( 2 or more? )

求值很简单，遇到操作符就连续弹出两个数字计算并且返回。



## Train in & out

`Key Point:` ==<font color = red>序列中的任何一个数其后面所有比它小的数应该是倒序的!</font>==

------

The answer can be in the form of a **tree**.$<x_1,x_2,...,x_n>$. $\Rightarrow$ ==Backtracking==!( How? )

By simulating the process of a train.(<font size = 2> *We may ignore other details and regard it as a Black Box. Thus, just input and output are needed.* </font>). Thus, we just need to consider the following 3 conditions:

- End state $\to$ print
- train out $\to$ pop $\to$ backtrack
- train in $\to$ push $\to$ backtrack

<div align = "center"><b>When to push and When to pop?</b></div>



```c++
    //  case 2: pop -> backtracking
    if( !station.empty() )
    {
        int temp = station.top();
        station.pop();
        result.push_back( temp );
        trainBacktracking( arrayTrain,FNum,SNum-1 );
        station.push( temp );
        result.pop_back();        
    }

    //  case 3: push -> backtracking
    if( FNum<sizeofTrain )
    {
        int temp = arrayTrain[ FNum ];
        station.push( temp );
        trainBacktracking( arrayTrain,FNum+1,SNum+1 );
        station.pop();
    }
```

------

> 对于类似的的进栈模拟，最终的数目N: $N = \frac{1}{n}C_{2n}^n$。
>
> （也叫卡特兰数）实际意义：对于序列中的任意元素$a_i$，排列在他后面并且比它小的元素必须呈逆序排序。


## 栈刷题

### [331. 验证二叉树的前序序列化](https://leetcode-cn.com/problems/verify-preorder-serialization-of-a-binary-tree/)

序列化二叉树的一种方法是使用前序遍历。当我们遇到一个非空节点时，我们可以记录下这个节点的值。如果它是一个空节点，我们可以使用一个标记值记录，例如 `#`。

```
	 _9_
    /   \
   3     2
  / \   / \
 4   1  #  6
/ \ / \   / \
# # # #   # #
```

例如，上面的二叉树可以被序列化为字符串 "9,3,4,#,#,1,#,#,2,#,6,#,#"，其中 # 代表一个空节点。

给定一串以逗号分隔的序列，验证它是否是正确的二叉树的前序序列化。编写一个在不重构树的条件下的可行算法。

每个以逗号分隔的字符或为一个整数或为一个表示 null 指针的 '#' 。

你可以认为输入格式总是有效的，例如它永远不会包含两个连续的逗号，比如 "1,,3" 。

------

（1）递归？

（2）官方题解用的是**栈**。

（3）How to improve the performance?

​	显然我们可以将叶子节点看成是`#`，非叶子节点看成是数字，非叶子节点始终比叶子节点多一。并且中间任意一个状态都有：$N(digit) \ge N('\#')$。反之，则说明到此为止已经是一个完整的子树了，矛盾。

​	这样忽略了栈的细节，仅仅考虑字符串的格式即可。



### 二叉树的遍历实现

用迭代的方法实现二叉树的三种遍历。本质是用栈模拟递归的过程。







### [1249. 移除无效的括号](https://leetcode-cn.com/problems/minimum-remove-to-make-valid-parentheses/)

```
输入：s = "lee(t(c)o)de)"
输出："lee(t(c)o)de"
解释："lee(t(co)de)" , "lee(t(c)ode)" 也是一个可行答案。
```

---

显然考虑栈。根据编译原理中的经验，遇到左括号可以执行入栈，反之出栈。

难点在于当我们遍历整个字符串的时候，我们不知道左右括号的数量关系（可能有三种）。因此一次遍历是不够的。我们需要考虑对应的左括号或者右括号的位置关系。我们可以用栈模拟括号对应的操作，同时使用另一个数据结构存储对应的括号的位置。

```c++
		stack< int > stack;
        set< int > bracketIndex;
```

`push` 和`pop`完成了一次成功的匹配，同时此时记录的是右括号失配的情况。

```c++
			if( s[i] == '(' ) stack.push(i);
            else if( s[i] == ')' )
            {
                if( stack.empty() ) bracketIndex.insert( i );
                else stack.pop();
            }
```

还要考虑左括号失配的情况。我们直接依次弹出栈元素即可。

```c++
       while( !stack.empty() )
        {
            bracketIndex.insert( stack.top() );
            stack.pop();
        }
```



### [32. 最长有效括号](https://leetcode-cn.com/problems/longest-valid-parentheses/)

给你一个只包含 `'('` 和 `')'` 的字符串，找出最长有效（格式正确且连续）括号子串的长度。

```
输入：s = "(()"
输出：2
解释：最长有效括号子串是 "()"
```

---

需要注意本题的要求，即满足题意的必须都是：`()()()()...()`的形式。

我们使用栈来解决它。基本的操作必然是***左进右出***。在此基础上，我们考虑其他细节。

我们假设字符串从`x[i]`处开始，到`x[j]`的时候失配。其实真正的记位应当为$j-(i-1)$，因为我们每一个成对的括号，压栈和弹栈相抵消。我们用栈来记录***字符串开始的位置***。

```c++
        for( int i = 0;i != s.length();i++ )
        {
            if( s[i] =='(' ) stack.push(i);
            else
            {
                stack.pop();
                if( stack.empty() ) stack.push(i);
                else res = max( res,i-stack.top() );
            }
        }
```



### [946. 验证栈序列](https://leetcode-cn.com/problems/validate-stack-sequences/)

```
输入：pushed = [1,2,3,4,5], popped = [4,5,3,2,1]
输出：true
解释：我们可以按以下顺序执行：
push(1), push(2), push(3), push(4), pop() -> 4,
push(5), pop() -> 5, pop() -> 3, pop() -> 2, pop() -> 1
```

---

就是栈模拟，可以特殊化梳理逻辑。

```c++
        if( pushed.size() != popped.size() ) return false;
        stack<int> stack;
        int i = 0, j = 0;
        for( ;i!=pushed.size();i++ )
        {
            stack.push( pushed[i] );
            while( !stack.empty() && stack.top()==popped[j] )
            {
                j++;
                stack.pop();
            }
        }
        return stack.empty();
```



## 队列刷题

### 单调队列

> ***“如果一个选手比你小还比你强，你就可以退役了。”**——单调队列的原理*

**单调队列**是一种主要用于解决**滑动窗口**类问题的数据结构。基本结构是双向队列$deque$，按照单调递减或递增的顺序存放。换言之，就是首尾的两个元素分别对应两个极端值。

当然，作为队列，所有的操作都只是在首尾完成的。

在这搬出百度百科的解释：不断地向缓存数组里读入元素，也不时地去掉最老的元素，不定期的询问当前缓存数组里的最小的元素。

用单调队列来解决问题，一般都是需要得到当前的某个范围内的最小值或最大值。



#### [239. 滑动窗口最大值](https://leetcode-cn.com/problems/sliding-window-maximum/)

给你一个整数数组 `nums`，有一个大小为 k 的滑动窗口从数组的最左侧移动到数组的最右侧。你只可以看到在滑动窗口内的 k 个数字。滑动窗口每次只向右移动一位。

返回滑动窗口中的最大值。

```
输入：nums = [1,3,-1,-3,5,3,6,7], k = 3
输出：[3,3,5,5,6,7]
解释：
滑动窗口的位置                最大值
---------------               -----
[1  3  -1] -3  5  3  6  7       3
 1 [3  -1  -3] 5  3  6  7       3
 1  3 [-1  -3  5] 3  6  7       5
 1  3  -1 [-3  5  3] 6  7       5
 1  3  -1  -3 [5  3  6] 7       6
 1  3  -1  -3  5 [3  6  7]      7
```

------

我们以此题为例，研究一下优先队列。由浅入深，先考虑暴力，即每次都从当前下标开始，无疑会做很多没用的事。由于我们只要知道最值就好，因此我们使用单调队列。考虑以下几点：

（1）我们这里假设单调队列是递减的。$q.front() \ge q[i] \ge q.back()$.

（2）每次遇到新的元素就想办法把它插入队列，同时注意==保持性质==：单调+该cache维护在合法的范围内。显然我们要分两步：

​		（2.1）插入队首/队尾。这里我们从尾部插入。

```c++
            while (!q.empty() && nums[i] >= nums[q.back()])	q.pop_back();
            q.push_back(i);
```

​		（2.2）调整队尾/队首。这里我们调整头部。

```c++
            queue.push_back( i );
            while( queue.front() <= i-k )	queue.pop_front();
            res.push_back( nums[queue.front()] );
```

<div align = 'center'><font color = indigo><B>一般默认还是以单调减为准。



