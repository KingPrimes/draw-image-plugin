package io.github.kingprimes.image;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

/**
 * 图片滤镜工具类
 * <p>提供图片滤镜处理工具，包括：</p>
 * <ul>
 *     <li>grayScale：将彩色图片转换为灰度图像</li>
 *     <li>sharpen：对图片应用锐化滤镜效果</li>
 *     <li>gaussianBlur：对图片应用高斯模糊滤镜效果</li>
 * </ul>
 *
 * @author KingPrimes
 * @version 1.0.0
 */
@SuppressWarnings("unused")
public final class FilterUtils {
    private FilterUtils() {
    }

    /**
     * 将彩色图片转换为灰度图像，保留原始图片的透明度通道
     * <p>灰度值计算采用标准 luminance 公式：<code>gray = 0.299*R + 0.587*G + 0.114*B</code>，符合人眼对不同颜色的敏感度</p>
     *
     * @param image 原始彩色图片对象（待转换的BufferedImage）
     * @return 转换后的灰度图像BufferedImage对象，尺寸与原始图片一致，格式为TYPE_INT_ARGB，透明度通道保持不变
     */
    public static BufferedImage grayScale(BufferedImage image) {
        int w = image.getWidth(), h = image.getHeight();
        int[] pixels = image.getRGB(0, 0, w, h, null, 0, w);
        for (int i = 0; i < pixels.length; i++) {
            int rgb = pixels[i];
            int gray = (int) (0.299 * ((rgb >> 16) & 0xFF)
                    + 0.587 * ((rgb >> 8) & 0xFF)
                    + 0.114 * (rgb & 0xFF));
            pixels[i] = (rgb & 0xFF000000) | (gray << 16) | (gray << 8) | gray;
        }
        BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        result.setRGB(0, 0, w, h, pixels, 0, w);
        return result;
    }

    /**
     * 对图片应用锐化滤镜效果，通过3x3卷积核增强图像边缘细节
     * <p>锐化原理：使用卷积矩阵对每个像素进行加权计算，中心像素权重为5，四邻域像素权重为-1，通过增强像素间对比度实现锐化</p>
     * <p>卷积核矩阵：
     * <pre>
     * [0, -1, 0]
     * [-1, 5, -1]
     * [0, -1, 0]
     * </pre>
     * 使用Java图像处理API的ConvolveOp执行卷积操作，保留原始图像尺寸和透明度通道
     *
     * @param image 原始图片对象（待锐化的BufferedImage）
     * @return 锐化处理后的新BufferedImage对象，尺寸与原始图片一致，格式为TYPE_INT_ARGB
     */
    public static BufferedImage sharpen(BufferedImage image) {
        float[] matrix = {
                0, -1, 0,
                -1, 5, -1,
                0, -1, 0
        };
        Kernel kernel = new Kernel(3, 3, matrix);
        ConvolveOp op = new ConvolveOp(kernel);
        return op.filter(image, new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB));
    }

    /**
     * 对图片应用高斯模糊效果，通过高斯核卷积实现图像平滑处理
     * <p>实现原理：基于高斯函数生成1D卷积核，通过两步分离卷积（先横向后纵向）高效实现高斯模糊，避免直接使用2D卷积的高计算复杂度</p>
     * <p>关键细节：</p>
     * <ul>
     *   <li>若模糊半径{@code radius}小于1，直接返回原始图片以避免无效计算</li>
     *   <li>卷积核尺寸为{@code 2*radius + 1}，半径越大模糊效果越强（计算量也随之增加）</li>
     *   <li>通过{@link #createGaussianKernel(int, int)}生成符合高斯分布的权重核</li>
     * </ul>
     *
     * @param image  原始图片对象（待模糊的BufferedImage）
     * @param radius 模糊半径（像素），控制模糊强度，需为非负整数（建议取值范围1-10，过大会显著增加计算耗时）
     * @return 模糊处理后的新BufferedImage对象，尺寸与原始图片一致，格式为TYPE_INT_ARGB；若输入半径无效则返回原始图片
     */
    public static BufferedImage gaussianBlur(BufferedImage image, int radius) {
        if (radius < 1) return image;
        int size = radius * 2 + 1;
        float[] kernel = createGaussianKernel(size, radius);
        BufferedImage hPass = convolve(image, kernel, size, 1);
        return convolve(hPass, kernel, 1, size);
    }

    private static float[] createGaussianKernel(int size, int radius) {
        float[] kernel = new float[size];
        float sigma = radius / 3.0f;
        float twoSigmaSquare = 2.0f * sigma * sigma;
        float sigmaRoot = (float) Math.sqrt(twoSigmaSquare * Math.PI);
        float total = 0.0f;

        for (int i = -radius; i <= radius; i++) {
            float distance = i * i;
            kernel[i + radius] = (float) Math.exp(-distance / twoSigmaSquare) / sigmaRoot;
            total += kernel[i + radius];
        }

        for (int i = 0; i < size; i++) {
            kernel[i] /= total;
        }
        return kernel;
    }

    private static BufferedImage convolve(BufferedImage image, float[] kernel, int kernelWidth, int kernelHeight) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        int kernelSizeX = kernelWidth;
        int kernelSizeY = kernelHeight;
        int halfKernelX = kernelSizeX / 2;
        int halfKernelY = kernelSizeY / 2;

        int[] pixels = new int[width * height];
        image.getRGB(0, 0, width, height, pixels, 0, width);
        int[] output = new int[width * height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                float r = 0, g = 0, b = 0, a = 0;
                for (int ky = 0; ky < kernelSizeY; ky++) {
                    for (int kx = 0; kx < kernelSizeX; kx++) {
                        int pixelX = Math.min(width - 1, Math.max(0, x + kx - halfKernelX));
                        int pixelY = Math.min(height - 1, Math.max(0, y + ky - halfKernelY));
                        int pixel = pixels[pixelY * width + pixelX];
                        float weight = kernel[ky * kernelSizeX + kx];

                        a += weight * ((pixel >> 24) & 0xFF);
                        r += weight * ((pixel >> 16) & 0xFF);
                        g += weight * ((pixel >> 8) & 0xFF);
                        b += weight * (pixel & 0xFF);
                    }
                }
                output[y * width + x] =
                        (((int) a & 0xFF) << 24) |
                                (((int) r & 0xFF) << 16) |
                                (((int) g & 0xFF) << 8) |
                                ((int) b & 0xFF);
            }
        }

        result.setRGB(0, 0, width, height, output, 0, width);
        return result;
    }
}