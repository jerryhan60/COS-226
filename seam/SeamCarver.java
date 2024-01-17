import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

import java.awt.Color;

public class SeamCarver {
    // this is for the picture of the seamcarver
    private Picture picture;
    // this is height of the picture
    private int H;
    // this is the width of the picture
    private int W;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        // copy initial image
        if (picture == null) throw new IllegalArgumentException();
        Picture pictureNow = new Picture(picture);
        this.picture = pictureNow;
        this.H = picture.height();
        this.W = picture.width();
    }

    // current picture
    public Picture picture() {
        return new Picture(picture);
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || y < 0 || x > W || y > H) throw new IllegalArgumentException();
        Color c1 = picture.get((x + W - 1) % picture.width(), y);
        Color c3 = picture.get((x + 1) % picture.width(), y);
        double xR = c1.getRed() - c3.getRed();
        double xG = c1.getGreen() - c3.getGreen();
        double xB = c1.getBlue() - c3.getBlue();
        double x0 = xR * xR + xG * xG + xB * xB;
        Color c2 = picture.get(x, (y + H - 1) % picture.height());
        Color c4 = picture.get(x, (y + 1) % picture.height());
        double yR = c2.getRed() - c4.getRed();
        double yG = c2.getGreen() - c4.getGreen();
        double yB = c2.getBlue() - c4.getBlue();
        double y0 = yR * yR + yG * yG + yB * yB;
        double energy = Math.sqrt(x0 + y0);
        return energy;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        double[][] E = new double[W][H];
        for (int i = 0; i < W; i++) {
            for (int j = 0; j < H; j++) {
                E[i][j] = energy(i, j);
            }
        }
        // sequence of indices for horizontal seam
        double[][] dp = new double[W][H];
        int[][] prev = new int[W][H];

        for (int i = 0; i < H; i++) {
            dp[0][i] = E[0][i];
            prev[0][i] = -1;
        }
        for (int i = 1; i < W; i++) {
            for (int j = 0; j < H; j++) {
                dp[i][j] = dp[i - 1][j] + E[i][j];
                prev[i][j] = j;
                if (j > 0) {
                    if (dp[i - 1][j - 1] + E[i][j] < dp[i][j]) {
                        dp[i][j] = dp[i - 1][j - 1] + E[i][j];
                        prev[i][j] = j - 1;
                    }
                }
                if (j < H - 1) {
                    if (dp[i - 1][j + 1] + E[i][j] < dp[i][j]) {
                        dp[i][j] = dp[i - 1][j + 1] + E[i][j];
                        prev[i][j] = j + 1;
                    }
                }
            }
        }
        int[] ans = new int[W];
        double best = Double.MAX_VALUE;
        for (int j = 0; j < H; j++) {
            if (dp[W - 1][j] < best) {
                best = dp[W - 1][j];
                ans[0] = j;
            }
        }
        for (int i = 1; i < W; i++) ans[i] = prev[W - i][ans[i - 1]];
        int[] finAns = new int[W];
        for (int i = 0; i < W; i++) {
            finAns[i] = ans[W - i - 1];
        }
        return finAns;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        double[][] E = new double[W][H];
        for (int i = 0; i < W; i++) {
            for (int j = 0; j < H; j++) {
                E[i][j] = energy(i, j);
            }
        }
        double[][] dp = new double[W][H];
        int[][] prev = new int[W][H];
        for (int i = 0; i < W; i++) {
            dp[i][H - 1] = E[i][H - 1];
            prev[i][0] = -1;
        }
        for (int j = H - 2; j >= 0; j--) {
            for (int i = 0; i < W; i++) {
                dp[i][j] = dp[i][j + 1] + E[i][j];
                prev[i][j] = i;
                if (i > 0) {
                    if (dp[i - 1][j + 1] + E[i][j] < dp[i][j]) {
                        dp[i][j] = dp[i - 1][j + 1] + E[i][j];
                        prev[i][j] = i - 1;
                    }
                }
                if (i < W - 1) {
                    if (dp[i + 1][j + 1] + E[i][j] < dp[i][j]) {
                        dp[i][j] = dp[i + 1][j + 1] + E[i][j];
                        prev[i][j] = i + 1;
                    }
                }
            }
        }
        int[] ans = new int[H];
        double best = Double.MAX_VALUE;
        for (int i = 0; i < W; i++) {
            if (dp[i][0] < best) {
                best = dp[i][0];
                ans[0] = i;

            }
        }
        for (int j = 1; j < H; j++) ans[j] = prev[ans[j - 1]][j - 1];
        return ans;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException("Null argument");
        if (seam.length != W) throw new IllegalArgumentException("Wrong array size");
        for (int i = 1; i < seam.length; i++) {
            if (seam[i] - seam[i - 1] != 1 && seam[i] - seam[i - 1] != 0
                    && seam[i] - seam[i - 1] != -1) {
                throw new IllegalArgumentException("wrong seam format");
            }
        }
        Picture newPicture = new Picture(picture.width(), picture.height() - 1);
        for (int i = 0; i < picture.width(); i++) {
            for (int j = 0; j < picture.height(); j++) {
                if (j < seam[i]) newPicture.set(i, j, picture.get(i, j));
                if (j > seam[i]) newPicture.set(i, j - 1, picture.get(i, j));
            }
        }
        H = H - 1;
        picture = newPicture;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException("Null argument");
        if (seam.length != H) throw new IllegalArgumentException("Wrong array size");
        for (int i = 1; i < seam.length; i++) {
            if (seam[i] - seam[i - 1] != 1 && seam[i] - seam[i - 1] != 0
                    && seam[i] - seam[i - 1] != -1) {
                throw new IllegalArgumentException("wrong seam format");
            }
        }
        Picture newPicture = new Picture(picture.width() - 1, picture.height());
        for (int j = 0; j < picture.height(); j++) {
            for (int i = 0; i < picture.width(); i++) {
                if (i < seam[j]) newPicture.set(i, j, picture.get(i, j));
                if (i > seam[j]) newPicture.set(i - 1, j, picture.get(i, j));
            }
        }
        W = W - 1;
        picture = newPicture;
    }

    //  unit testing (required)
    public static void main(String[] args) {
        Picture pic = new Picture("city8000-by-2000.png");
        SeamCarver seam = new SeamCarver(pic);
        Stopwatch timer = new Stopwatch();
        seam.removeVerticalSeam(seam.findVerticalSeam());
        seam.removeHorizontalSeam(seam.findHorizontalSeam());
        StdOut.println("elapsed time =     " + timer.elapsedTime());

        /*
        StdOut.println("This is the horizontal Seam");
        for (int i = 0; i < seam.W; i++) StdOut.println(seam.findHorizontalSeam()[i]);
        StdOut.println("This is the vertical Seam");
        for (int i = 0; i < seam.H; i++) StdOut.println(seam.findVerticalSeam()[i]);
        seam.removeVerticalSeam(seam.findVerticalSeam());
        StdOut.println("These are the new energies after removing vertical seam");
        for (int i = 0; i < seam.W; i++) {
            for (int j = 0; j < seam.H; j++) {
                StdOut.println(seam.energy(i, j));
            }
        }
        seam.removeHorizontalSeam(seam.findHorizontalSeam());
        StdOut.println(seam.picture.width() + " x " + seam.picture.height());
        */
    }
}
