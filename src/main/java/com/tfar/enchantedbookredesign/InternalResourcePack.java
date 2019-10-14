package com.tfar.enchantedbookredesign;

import com.google.common.collect.ImmutableSet;
import net.minecraft.resources.IResourcePack;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.resources.data.IMetadataSectionSerializer;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class InternalResourcePack implements IResourcePack{

  @Override
  public InputStream getResourceStream(ResourcePackType type, ResourceLocation location) throws IOException {
    return null;
  }

  @Override
  public boolean resourceExists(ResourcePackType type, ResourceLocation location) {
    return type == ResourcePackType.CLIENT_RESOURCES && ClassLoader.getSystemResource(location.getPath()) != null;
  }

  @Override
  public Set<String> getResourceNamespaces(ResourcePackType type) {
    return type == ResourcePackType.CLIENT_RESOURCES ? ImmutableSet.of(EnchantedBookRedesign.MODID) : Collections.emptySet();
  }

  @Override
  public <T> T getMetadata(IMetadataSectionSerializer<T> arg0) throws IOException {
    return null;
  }

  @Override
  public String getName() {
    return "Enchanted Book Redesign Resource Pack";
  }

  @Override
  public Collection<ResourceLocation> getAllResourceLocations(ResourcePackType type, String pathIn, int maxDepth, java.util.function.Predicate<String> filter) {
    return Collections.emptyList();
  }

  @Override
  public InputStream getRootResourceStream(String arg0) throws IOException {
    return ClassLoader.getSystemResourceAsStream("assets/" + arg0);
  }

  @Override
  public void close() throws IOException {}
}
