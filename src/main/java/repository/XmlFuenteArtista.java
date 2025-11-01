package repository;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import domain.Artista;

public class XmlFuenteArtista implements FuenteArtista {

  private final Path path;

  public XmlFuenteArtista(Path path) {
    this.path = Objects.requireNonNull(path, "path");
  }

  public Path getPath() {
    return path;
  }

  @Override
  public String toString() {
    return "XmlFuenteArtista{path=" + path + "}";
  }

  @Override
  public List<Artista> cargar() {
	// TODO Auto-generated method stub
	return null;
  }

  @Override
  public void guardar(List<Artista> artistas) {
	// TODO Auto-generated method stub
	
  }
}
