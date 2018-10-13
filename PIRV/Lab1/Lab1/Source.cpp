#include <omp.h>
#include <iostream>
#include <random>
#include <time.h>
#include <iomanip>
#include <chrono>
#include <fstream>

using namespace std;

ofstream fout;

class Matrix
{
private:
	int n = 1, m = 1;
	double** matrix;
public:
	int getN() { return n; }
	int getM() { return m; }
	void setN(int N) { n = N; }
	void setM(int M) { m = M; }
	double** getMatrix() { return matrix; }
	void setMatrix(double** Matrix) { matrix = Matrix; }
	
	Matrix() {}
	Matrix(int N, int M)
	{
		n = N;
		m = M;
		matrix = new double*[n];
		for (int i = 0; i < n; i++)
		{
			matrix[i] = new double[M];
			for (int j = 0; j < m; j++)
				matrix[i][j] = 0;
		}
	}

};

void PrintMatrix(Matrix matrix)
{
	double** matr = matrix.getMatrix();
	for (int i = 0; i < matrix.getN(); i++)
	{
		for (int j = 0; j < matrix.getM(); j++)
			std::cout<<setprecision(8) << matr[i][j] << " ";
		std::cout << std::endl;
	}
}
double** GetRandomMatrix(int n, int m)
{
	double** matrix;
	matrix = new double*[n];
	for (int i = 0; i < n; i++)
	{
		matrix[i] = new double[m];
		for (int j = 0; j < m; j++)
		{
			matrix[i][j] = rand() % 200 - 100;
		}
	}
	return matrix;
}
Matrix PointMultiplication(Matrix matrix1,Matrix matrix2, int status)
{
	int N1 = matrix1.getN(), N2 = matrix1.getM(), N3 = matrix2.getM();
	Matrix result(N1, N3);
	double** mas1 = matrix1.getMatrix(), ** mas2 = matrix2.getMatrix(),** mas3 = result.getMatrix();
	
	#pragma omp parallel for if(status==1)
	for (int i = 0; i < N1; i++)
	{
			#pragma omp parallel for if(status==2)
			for (int k = 0; k < N3; k++)
			{
				for (int j = 0; j < N2; j++)
				{
					mas3[i][k] += mas1[i][j] * mas2[j][k];
				}
			}
	}
	return result;
}
Matrix BlockMultiplaction(Matrix matrix1, Matrix matrix2, int r, int status)//r=size of block
{
	if (r == 1)
		return PointMultiplication(matrix1, matrix2,status);
	int N1 = matrix1.getN(), N2 = matrix1.getM(), N3 = matrix2.getM(), r1 = N1 / r, r2 = N2 / r, r3=N3 / r;
	Matrix result(N1, N3);
	double** mas1 = matrix1.getMatrix(), ** mas2 = matrix2.getMatrix(), ** mas3 = result.getMatrix();
	
	#pragma omp parallel for if(status==1)
	for (int i1 = 0; i1 < r1; i1++)
	{
		#pragma omp parallel for if(status==2)
		for (int k1 = 0; k1 < r3; k1++)
		{
			for (int j1 = 0; j1 < r2; j1++)
			{
				for (int i2 = 0; i2 < r; i2++)
				{
					for (int k2 = 0; k2 < r; k2++)
					{
						for (int j2 = 0; j2 < r; j2++)
						{
							int i = i1*r + i2;
							int j = j1*r + j2;
							int k = k1*r + k2;
							mas3[i][k] += mas1[i][j] * mas2[j][k];
						}
					}
				}
			}
		}
	}

	return result;
}

int main(int argc, char* argv[])
{
	int N1 = atoi(argv[1]), N2 = atoi(argv[2]), N3 = atoi(argv[3]), r = atoi(argv[4]), status=atoi(argv[5]);/*, numThreads=atoi(argv[5]);*/
	
	srand(time(NULL));
	omp_set_dynamic(0);
	omp_set_num_threads(8);

	Matrix matrix1(N1, N2), matrix2(N2, N3), resultPoint,resultBlock;
	matrix1.setMatrix(GetRandomMatrix(N1, N2));
	matrix2.setMatrix(GetRandomMatrix(N2, N3));
		
	auto start = chrono::high_resolution_clock::now();
	resultBlock = BlockMultiplaction(matrix1, matrix2, r, status);
	auto end = chrono::high_resolution_clock::now();

	auto search=chrono::duration_cast<chrono::milliseconds>(end - start).count() / 1000.;

	fout.open("output.txt", fstream::out | fstream::app);
	fout << setprecision(8) << search<<" ";
	fout.close();
	fout.flush();


}

