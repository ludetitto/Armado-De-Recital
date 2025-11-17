package repository;

import java.nio.file.Path;
import java.util.Objects;

public class XmlFuenteRecital implements FuenteRecital {

  private final Path path;

  public XmlFuenteRecital(Path path) {
    this.path = Objects.requireNonNull(path, "path");
  }

  public Path getPath() {
    return path;
  }

  @Override
  public String toString() {
    return "XmlFuenteRecital{path=" + path + "}";
  }

  @Override
  public RecitalRepository cargar() {
	// TODO Auto-generated method stub
	return null;
  }

  @Override
  public void guardar(RecitalRepository recital) {
	// TODO Auto-generated method stub
	
  }
}
