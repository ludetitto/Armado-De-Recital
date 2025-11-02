package repository;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import domain.Cancion;

public class XmlFuenteCancion implements FuenteCancion {

  private final Path path;

  public XmlFuenteCancion(Path path) {
    this.path = Objects.requireNonNull(path, "path");
  }

  public Path getPath() {
    return path;
  }

  @Override
  public String toString() {
    return "XmlFuenteCancion{path=" + path + "}";
  }

  @Override
  public List<Cancion> cargar() {
	// TODO Auto-generated method stub
	return null;
  }

  @Override
  public void guardar(List<Cancion> canciones) {
	// TODO Auto-generated method stub
	
  }
}
