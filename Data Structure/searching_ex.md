[TOC]

# 查找刷题



## Binary Search

### [162. 寻找峰值](https://leetcode-cn.com/problems/find-peak-element/)

峰值元素是指其值严格大于左右相邻值的元素。

给你一个整数数组 nums，找到峰值元素并返回其索引。数组可能包含多个峰值，在这种情况下，返回 任何一个峰值 所在位置即可。

你可以假设 nums[-1] = nums[n] = -∞ 。

你必须实现时间复杂度为 O(log n) 的算法来解决此问题。

---

关键是二分怎么想到。这里关键的是`nums[-1] = nums[n] = -∞`。再比较中点为界前后两个点的情况（无非2种，其实是对称的）就可以得出，类似极大值。

```c++
                while( left<right )
                {
                    int mid = left + (right-left)/2;
                    if( nums[mid]>=nums[mid+1] ) right = mid;
                    else left = mid+1;
                }
                return left;
```









## AVL

### [110. 平衡二叉树](https://leetcode-cn.com/problems/balanced-binary-tree/)

给定一个二叉树，判断它是否是高度平衡的二叉树。

---

递归判断。递归计算左右子树的高度求得。

```c++
    int height( TreeNode* root )
    {
        if(!root) return 0;
        int left = height( root->left );
        int right = height( root->right );

        if( left==-1 || right==-1 || abs(left-right)>1 ) return -1;
        return max(left,right)+1;
    }
```

