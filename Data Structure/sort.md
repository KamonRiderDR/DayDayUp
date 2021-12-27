# Sort

[TOC]


$$
\textbf{DR \ \ \ \ \ \ \ \ \ \ 2021/11/19 \ \ \ \ \ \ \ \ \ \ V1.0}
\\
-------------------------------
$$



<img src="../Data Structure/image/sort_01.jpg" style="zoom:20%;" />

（记忆：关于稳定，事实上大多数跳转访问的排序方法都是不稳定的，那些依次访问的多为稳定排序）





### 交换排序（冒泡、快排）

所谓交换排序，是指比较两个元素的结果并进行交换的操作。

冒泡略。暴力，平方，稳定。

:star2:**快速排序**





### 选择排序（简单选择、==堆排序==）

包括了简单选择排序和堆排序。

简单选择排序就是每一次都选择一个当前最小的。（$space: O(n);time: O(n^2)$）。不稳定。

**堆排序**

思路：

- 初始化堆，结束后最大值会冒上去。（`自底向上`实现）

```c++
void build( int arr[],int length )
{
    for( int i = length/2;i>0;i--  )	adjust( arr,i,length );
}
```

- **排序**：输出堆顶元素（最大的就放在最后），然后将末位元素送至堆顶，调整。

调整的过程是**自顶向下**的，也就是从根向孩子节点去调整。

```c++
void adjust(int arr[], int index, int index)
{
	arr[0] = arr[index];
    for( int i = 2*index;index <= length;index*=2 )
    {
        if( i<length && arr[i]<arr[i+1] )	i++;
        
        if( arr[0] >= arr[i] ) break;
        else{
        	arr[index] = arr[i];
        	index = i;
        }
     }
     arr[index] = arr[0];
}
```

（调整的算法以严版为准）这是一次调整的过程。从根开始向下，对于子节点，同时满足：`max{child[i]}=k`、`k <= *root`则执行`swap()`操作。这显然是一个大根堆（大的作为根）

> 为什么只要沿着交换的那个子树就可以了呢？万一会出现另一棵子树有小节点呢？
>
> Enough！因为我们初始化的时候是`自底向上`的，即对于节点的每一棵子树，都是子树为根的树的最小节点。

最终的`sort()`函数：

```c++
void heapSort(int arr[],int length)
{
    build(arr,length);
    for( int i = length; i>1;i-- )
    {
        swap( arr[i],arr[1] );
        adjust(arr,1,i-1);
    }
}
```

该算法同样不稳定。

可以看出，每经过一次排序，就能固定一个元素的位置。（而且是最大的）

（`2010`）几个概念：（1）完全二叉树。（2）顺序存储。（3）次大值一定在第二层。



### 归并排序

**基本思想**：将有序的子表合成新的有序表。（`merge()`）该子操作直接顺序比较就好了。 

> （记忆的话，就想象成两个堆，每次只弹一个最小值（`min{heap1,heap2}`）出来）

`2路归并排序`：**二分**。

```c++
void mergeSort( int arr[],int low,int high )
{
    int mid = (low+high)/2;
    if( low<high )
    {
        mergeSort(arr,low,mid);
        mergeSort(arr,mid+1,high);
        merge(arr,low,mid,high);
    }
}
```

**性质分析：**空间（$O(n)$）、时间（$O(n\log_2 n)$）、稳定。

显然， 经过一次归并排序后，不一定有元素定下自己的位置。



### 基数排序（桶排序）

**基本思想：**基于多关键字的排序。（比如个位、十位、百位）

特定场景，因为要确保单关键字可以拆分成多个关键字。

过程：略。（这里用的是个十百位依次排序）

<img src="../Data Structure/image/sort_02.jpg" style="zoom:13%;" />

<img src="../Data Structure/image/sort_03.jpg" style="zoom:11%;" />

<img src="../Data Structure/image/sort_04.jpg" style="zoom:11%;" />



需要辅助的桶（链表队列），因此空间（$O(r)$）。时间上是（$O(d(n+r))$）。

该算法是稳定的。



### 外部排序（多路归并排序 && Loser Tree）

skip.