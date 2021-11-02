[TOC]

# Array && Matrix

### [搜索二维矩阵 II](https://leetcode-cn.com/problems/search-a-2d-matrix-ii/)

编写一个高效的算法来搜索 m x n 矩阵 matrix 中的一个目标值 target 。该矩阵具有以下特性：

每行的元素从左到右升序排列。
每列的元素从上到下升序排列。

```
输入：matrix = [[1,4,7,11,15],[2,5,8,12,19],[3,6,9,16,22],[10,13,14,17,24],[18,21,23,26,30]], target = 5
输出：true
```

**思路分析**

<img src=".\image\2D_matrix.png" style="zoom: 50%;" />

作为变式，和Ⅰ有明显的不同。Ⅰ中，矩阵是严格递增的，此处不是。

我们可以将左上角看成A区域，右下角看成B区域，图中的箭头表示了数字大小增加的方向。

我们从左下角开始（对称的右上角也可）遍历。

> 还有另一种解释，即每一个节点，上方比它小，右方比它大，等价于L型的BST。

```c++
class Solution {
public:
    bool searchMatrix(vector<vector<int>>& matrix, int target) {
        if( matrix.empty() || matrix[0].empty() ) return false;
        int x = matrix.size()-1;
        int y = 0;

        while( x>=0 && y<matrix[0].size() )
        {
            if( matrix[x][y]>target ) x--;
            else if( matrix[x][y]<target ) y++;
            else return true;
        }
        return false;       
    }
};
```

### [581. 最短无序连续子数组](https://leetcode-cn.com/problems/shortest-unsorted-continuous-subarray/)

给你一个整数数组 nums ，你需要找出一个 连续子数组 ，如果对这个子数组进行升序排序，那么整个数组都会变为升序排序。

请你找出符合题意的**最短**子数组，并输出它的长度。

```
输入：nums = [2,6,4,8,10,9,15]
输出：5
解释：你只需要对 [6, 4, 8, 10, 9] 进行升序排序，那么整个表都会变为升序排序。
```

---

我们可以将最终的结果分为三段：$<nums_1,nums_2,nums_3>$。当我们对于第二段排好序以后，我们就有了整个有序的数组。

有一个显然的等式关系：
$$
nums_1[i]\le nums_1[left-1]\le \min\{nums_2\}< \max\{nums_2\}\le nums_3[right+1]
$$

- 官方给出的题解是将左右两个端点分开来看待：对于`left`，要小于它右边的所有值（最小值）。`right`则正好反过来。因此，其实两次***互 不 相 关 的 遍 历***我们可以在*一次*遍历就完成。

  ```c++
          for( int i = 0;i != n;i++ )
          {
              //	first right
              if( maxVal<=nums[i] ) maxVal = nums[i];
              else right = i;
              
              //	then left
              if( minVal>=nums[n-1 - i] ) minVal = nums[n-1-i];
              else left = n-1-i;
          }
  ```


