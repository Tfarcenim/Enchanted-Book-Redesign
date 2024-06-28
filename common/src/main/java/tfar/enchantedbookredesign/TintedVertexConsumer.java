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

	public VertexConsumer vertex(double x, double y, double z) {
		this.vertexConsumer.vertex(x, y, z);
		return this;
	}

	public VertexConsumer color(int red, int green, int blue, int alpha) {
		int r = color >> 16 & 0xff;
		int g = color >> 8 & 0xff;
		int b = color & 0xff;
		this.vertexConsumer.color(r, g, b, alpha);
		return this;
	}

	public VertexConsumer uv(float u, float v) {
		this.vertexConsumer.uv(u, v);
		return this;
	}

	public VertexConsumer overlayCoords(int u, int v) {
		this.vertexConsumer.overlayCoords(u, v);
		return this;
	}

	public VertexConsumer uv2(int u, int v) {
		this.vertexConsumer.uv2(u, v);
		return this;
	}

	public VertexConsumer normal(float x, float y, float z) {
		this.vertexConsumer.normal(x, y, z);
		return this;
	}

	public void vertex(float x, float y, float z, float red, float green, float blue, float alpha, float u, float v, int overlay, int light, float normalX, float normalY, float normalZ) {

		float r = (color >> 16 & 0xff) / 255f;
		float g = (color >> 8 & 0xff) / 255f;
		float b = (color & 0xff) / 255f;

		this.vertexConsumer.vertex(x, y, z, r, g, b, alpha, u, v, overlay, light, normalX, normalY, normalZ);
	}

	@Override
	public VertexConsumer color(int pColorARGB) {
		return VertexConsumer.super.color(pColorARGB);
	}

	@Override
	public VertexConsumer color(float pRed, float pGreen, float pBlue, float pAlpha) {
		return VertexConsumer.super.color(pRed, pGreen, pBlue, pAlpha);
	}

	@Override
	public void defaultColor(int p_166901_, int p_166902_, int p_166903_, int p_166904_) {
		vertexConsumer.defaultColor(p_166901_, p_166902_, p_166903_, p_166904_);
	}

	@Override
	public void unsetDefaultColor() {
		vertexConsumer.unsetDefaultColor();
	}

	public void endVertex() {
		this.vertexConsumer.endVertex();
	}
}
