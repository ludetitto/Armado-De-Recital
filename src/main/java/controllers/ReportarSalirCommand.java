package controllers;

import java.nio.file.Path;
import repository.RecitalRepository;
import services.RecitalService;

public class ReportarSalirCommand implements ComandoContratacion {

	private final Path path;
	private final RecitalRepository recitalRepository;
	private final RecitalService recitalService;
	

	public ReportarSalirCommand(Path path, RecitalRepository recitalRepository, RecitalService recitalService) {
		this.path = path;
		this.recitalRepository = recitalRepository;
		this.recitalService = recitalService;
	}

	@Override
    public void ejecutar() {
		recitalService.reportarEstadoActual(recitalRepository, path);
    }

    @Override
    public void deshacer() {
        // Listado: no hay nada que deshacer
    }
}