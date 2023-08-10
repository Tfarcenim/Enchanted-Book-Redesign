package tfar.enchantedbookredesign;

import com.mojang.blaze3d.vertex.IVertexBuilder;

public class TintedVertexConsumer implements IVertexBuilder {

	public static IVertexBuilder withTint(IVertexBuilder tint, int color) {
		return new TintedVertexConsumer(tint, color);
	}

	private final IVertexBuilder vertexConsumer;
	private final int color;

	public TintedVertexConsumer(IVertexBuilder vertexConsumer, int color) {

		this.vertexConsumer = vertexConsumer;
		this.color = color;
	}

	public IVertexBuilder vertex(double x, double y, double z) {
		this.vertexConsumer.vertex(x, y, z);
		return this;
	}

	public IVertexBuilder color(int red, int green, int blue, int alpha) {
		int r = color >> 16 & 0xff;
		int g = color >> 8 & 0xff;
		int b = color & 0xff;
		this.vertexConsumer.color(r, g, b, alpha);
		return this;
	}

	public IVertexBuilder uv(float u, float v) {
		this.vertexConsumer.uv(u, v);
		return this;
	}

	public IVertexBuilder overlayCoords(int u, int v) {
		this.vertexConsumer.overlayCoords(u, v);
		return this;
	}

	public IVertexBuilder uv2(int u, int v) {
		this.vertexConsumer.uv2(u, v);
		return this;
	}

	public IVertexBuilder normal(float x, float y, float z) {
		this.vertexConsumer.normal(x, y, z);
		return this;
	}

	public void vertex(float x, float y, float z, float red, float green, float blue, float alpha, float u, float v, int overlay, int light, float normalX, float normalY, float normalZ) {

		float r = (color >> 16 & 0xff) / 255f;
		float g = (color >> 8 & 0xff) / 255f;
		float b = (color & 0xff) / 255f;

		this.vertexConsumer.vertex(x, y, z, r, g, b, alpha, u, v, overlay, light, normalX, normalY, normalZ);
	}

	public void endVertex() {
		this.vertexConsumer.endVertex();
	}
}
