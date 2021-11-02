#include<iostream>
#include <stack>

using namespace std;

void printTrain(int *a,int n)
{
	//a[n]输出最后的序列，n表示火车的编号
	//a[n]表示的是最后出站的顺序，所以倒推
	stack<int> trainIn;

	bool flag = true;
	int currentTrainNumber = 1;
	int numberOfArray = 0;

	while (numberOfArray < n)
	{
		if (a[numberOfArray] == currentTrainNumber)
		{
			//表示火车港进站就出站，符合题意
			numberOfArray++;
			currentTrainNumber++;
		}
		else if (numberOfArray<n && a[numberOfArray] == trainIn.top() && ! (trainIn.empty()) )
		{
			//表示出站的是最外面的火车，符合题意
			
			trainIn.pop();
			numberOfArray++;
		}
		else if(currentTrainNumber <= n)
		{
			//把火车全部压进栈
			trainIn.push(currentTrainNumber);
			currentTrainNumber++;
		}
		else
		{
			flag = false;
			break;
		}
	}

	if (flag)
	{
		cout << "Yes! ";
		for (int i = 0; i < n; i++)
		{
			cout << a[i] << " ";
		}
		cout << endl;
	}
}

//递归法获得火车的全排列
void sort(int* a, int k, int n)
{
	if (k == n - 1)
	{
		printTrain(a, n);
	}
	else
	{
		for (int i = k; i < n; i++)
		{
			int temp = a[i];
			a[i] = a[k];
			a[k] = temp;

			sort(a, k + 1, n);

			temp = a[i];
			a[i] = a[k];
			a[k] = temp;
		}
	}

}

int main()
{
	int n = 0;
	while (cin>>n)
	{
		int* train = new int[n];
		//火车的编号从1开始
		for (int i = 0; i < n; i++)
		{
			train[i] = i + 1;
		}

		sort(train, 0, n);

		//
		delete[]train;
		cout << "Destructor completed!\n";
	}

	system("pause");
	return 0;
}