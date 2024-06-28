package tfar.enchantedbookredesign;

import com.mojang.blaze3d.vertex.VertexConsumer;

public class TintedVertexConsumer implements VertexConsumer {

	public static VertexConsumer withTint(VertexConsumer tint, int color) {
		return new TintedVertexConsumer(tint, color);
	}

	private final VertexConsumer vertexConsumer;
	private final int color;

	public TintedVertexConsumer(VertexConsumer vertexConsumer, int color) {

		this.vertexConsumer = vertexConsumer;
		this.color = color;
	}

	@Override
    public VertexConsumer addVertex(float x, float y, float z) {
		this.vertexConsumer.addVertex(x, y, z);
		return this;
	}

	@Override
    public VertexConsumer setColor(int red, int green, int blue, int alpha) {
		int r = color >> 16 & 0xff;
		int g = color >> 8 & 0xff;
		int b = color & 0xff;
		this.vertexConsumer.setColor(r, g, b, alpha);
		return this;
	}

	@Override
    public VertexConsumer setUv(float u, float v) {
		this.vertexConsumer.setUv(u, v);
		return this;
	}

	@Override
    public VertexConsumer setUv1(int u, int v) {
		this.vertexConsumer.setUv1(u, v);
		return this;
	}

	@Override
    public VertexConsumer setUv2(int u, int v) {
		this.vertexConsumer.setUv2(u, v);
		return this;
	}

	@Override
    public VertexConsumer setNormal(float x, float y, float z) {
		this.vertexConsumer.setNormal(x, y, z);
		return this;
	}
}
