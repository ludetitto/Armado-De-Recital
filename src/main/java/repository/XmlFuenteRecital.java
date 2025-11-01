package repository;

import java.nio.file.Path;
import java.util.List;
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
  public List<RecitalRepository> cargar() {
	// TODO Auto-generated method stub
	return null;
  }

  @Override
  public void guardar(List<RecitalRepository> recitales) {
	// TODO Auto-generated method stub
	
  }
}
