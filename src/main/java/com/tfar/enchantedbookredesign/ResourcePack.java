package com.tfar.enchantedbookredesign;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.IResourcePack;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.resources.data.IMetadataSectionSerializer;
import net.minecraft.resources.data.PackMetadataSection;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.resource.IResourceType;
import net.minecraftforge.resource.ISelectiveResourceReloadListener;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class ResourcePack implements IResourcePack, ISelectiveResourceReloadListener {

  protected boolean validPath(ResourceLocation location) {
    return true;
  }

  private final Map<ResourceLocation, String> loadedData = new HashMap<>();

  @Override
  public InputStream getResourceStream(ResourcePackType type, ResourceLocation location) throws IOException {
    if (type == ResourcePackType.CLIENT_RESOURCES && validPath(location)) {
      String s = loadedData.computeIfAbsent(location, loc -> {
        InputStream in = ClassLoader.getSystemResourceAsStream(location.getPath());
        StringBuilder data = new StringBuilder();
        Scanner scan = new Scanner(in);
        try {
          while (scan.hasNextLine()) {
            data.append(scan.nextLine().replaceAll("@radius@", Integer.toString(0))).append('\n');
          }
        } finally {
          scan.close();
        }
        return data.toString();
      });

      return new ByteArrayInputStream(s.getBytes());
    }
    throw new FileNotFoundException(location.toString());
  }

  @Override
  public boolean resourceExists(ResourcePackType type, ResourceLocation location) {
    return type == ResourcePackType.CLIENT_RESOURCES && validPath(location) && ClassLoader.getSystemResource(location.getPath()) != null;
  }

  @Override
  public Set<String> getResourceNamespaces(ResourcePackType type) {
    return type == ResourcePackType.CLIENT_RESOURCES ? ImmutableSet.of(EnchantedBookRedesign.MODID) : Collections.emptySet();
  }

  @SuppressWarnings({ "unchecked", "null" })
  @Override
  public <T> T getMetadata(IMetadataSectionSerializer<T> arg0) throws IOException {
    if ("pack".equals(arg0.getSectionName())) {
      return (T) new PackMetadataSection(new StringTextComponent("Ehcnated"), 3);
    }
    return null;
  }

  @Override
  public String getName() {
    return "Enchanted Book Redesign Resource Pack";
  }

  @Override
  public void onResourceManagerReload(IResourceManager resourceManager, java.util.function.Predicate<IResourceType> resourcePredicate) {
    loadedData.clear();
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
