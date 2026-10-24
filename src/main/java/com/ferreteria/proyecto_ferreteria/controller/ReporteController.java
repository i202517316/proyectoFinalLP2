package com.ferreteria.proyecto_ferreteria.controller;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ferreteria.proyecto_ferreteria.service.ReporteService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

@RestController
@RequestMapping("reporte")
@RequiredArgsConstructor
public class ReporteController {

	private final ReporteService reporteService;

	@GetMapping("boleta/{numBoleta}")
	public void boleta(@PathVariable Integer numBoleta, HttpServletResponse response) throws Exception {
		String reportPath = "/reportes/boleta/boleta.jrxml";

		Map<String, Object> params = new HashMap<>();
		params.put("pNumBoleta", numBoleta);
		
		JasperPrint jasperPrint = reporteService.getJasperPrint(params, reportPath);

		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", String.format("inline; filename=boleta-nro-%s.pdf", numBoleta));

		OutputStream outputStream = response.getOutputStream();
		JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);

		outputStream.flush();
		outputStream.close();
	}
}
