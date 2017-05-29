package hw_segmented_least_square;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	
	private ArrayList<Point> points = new ArrayList<>();
	private double c;
	
	class Point {
		public double x, y;
		
		Point(double x, double y) {
			this.x = x;
			this.y = y;
		}
	}

	public static void main(String[] args) {
		Main main = new Main();
		main.run();
	}
	
	private void run() {
		addPoints();
		Scanner scanner = new Scanner(System.in);
		System.out.print("enter c: ");
		c = scanner.nextDouble();
		scanner.close();
		
		calculateSegmentedLeastSquares();
	}
	
	private void addPoints() {
		try {
			FileInputStream fileInputStream = new FileInputStream(new File("Points.txt"));
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, Charset.forName("UTF-8"));
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String line;

			while ((line = bufferedReader.readLine()) != null) {
				String pointsArray[] = line.trim().split(" +");
				Point point = new Point(Double.valueOf(pointsArray[0]), Double.valueOf(pointsArray[1]));
				points.add(point);
			}

			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void calculateSegmentedLeastSquares() {
		int n = points.size();
		double opt[] = new double[n + 1];
		int opt_segment[] = new int[n + 1];
		double slope[][] = new double[n + 1][n + 1];
		double intercept[][] = new double[n + 1][n + 1];
		double e[][] = new double[n + 1][n + 1];
		
		opt[0] = opt_segment[0] = 0;
		
		for (int j = 1; j <= n; j++) {
			for (int i = 1; i <= j; i++) {
				double a = getA(i, j);
				double b = getB(i, j, a);
				double sse = getSSE(i, j, a, b); 
				slope[i][j] = a;
				intercept[i][j] = b;
				e[i][j] = sse;
			}
		}
		
		for (int j = 1; j <= n; j++) {
			double minCost = Double.POSITIVE_INFINITY;
			double temp = 0;
			int k = 0;
			for (int i = 1; i <= j; i++) {
				temp = e[i][j] + c + opt[i - 1];
				if (temp < minCost) {
					minCost = temp;
					k = i;
				}
			}
			opt[j] = minCost;
			opt_segment[j] = k;
		}
		
		for (int j = n, i = opt_segment[n]; j > 0; j = i - 1, i = opt_segment[j]) {
			System.out.println(i + "   " + j + "   " + slope[i][j] + " " + intercept[i][j]);
		}
	}
	
	private double getA(int i, int j) {
		double sigma1 = 0, sigma2 = 0, sigma3 = 0, sigma4 = 0, sigma5 = 0;
		
		for (int a = i; a <= j; a++) {
			sigma1 += points.get(a - 1).x * points.get(a - 1).y;
			sigma2 += points.get(a - 1).x;
			sigma3 += points.get(a - 1).y;
			sigma4 += Math.pow(points.get(a - 1).x, 2);
		}
		
		sigma5 = sigma2;
		
		return (((points.size() * sigma1) - (sigma2 * sigma3)) / ((points.size() * sigma4) - Math.pow(sigma5, 2)));
	}
	
	private double getB(int i, int j, double a) {
		double sigma1 = 0, sigma2 = 0;
		
		for (int b = i; b <= j; b++) {
			sigma1 += points.get(b - 1).y;
			sigma2 += points.get(b - 1).x;
		}
		
		return (sigma1 - (a * sigma2)) / points.size();
	}
	
	private double getSSE(int i, int j, double a, double b) {
		double sse = 0;
		
		for (int s = i; s <= j; s++) {
			double calculation = Math.pow(points.get(s - 1).y - a * points.get(s - 1).x - b, 2);
			
			sse += calculation;
		}
		
		return sse;
	}

}

