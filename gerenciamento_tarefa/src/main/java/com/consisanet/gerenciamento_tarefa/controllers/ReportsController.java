package com.consisanet.gerenciamento_tarefa.controllers;

import com.consisanet.gerenciamento_tarefa.dtos.ReportsMapperDto;
import com.consisanet.gerenciamento_tarefa.dtos.TarefaDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;

import java.util.*;

@RestController
@RequestMapping("api/v1/reports")
@Api(value = "Endpoints para gerar relatório.")
public class ReportsController {

    @ApiOperation(value = "Gera relatório.", httpMethod = "POST")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Sucesso ao gerar o relatório.")
            }
    )
    @PostMapping(produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> tarefaReports(@RequestBody List<TarefaDto> tarefaDtos) throws JRException, IOException {

        InputStream jasperStream = new ClassPathResource("reportsModels/reports-tarefa.jasper").getInputStream();

        List<ReportsMapperDto> dataOfReport = new ArrayList<>();

        tarefaDtos.forEach(t -> {
            ReportsMapperDto tarefa = new ReportsMapperDto();

            tarefa.setId(t.getId());
            tarefa.setNomeTarefa(t.getNomeTarefa());
            if(t.getTarefaPrincipal() != null)
                tarefa.setTarefaPrincipal(t.getTarefaPrincipal().getNomeTarefa());
            if(t.getDataFim() != null)
                tarefa.setDataFim(t.getDataFim().toString());
            if(t.getUsuarioResponsavel() != null)
                tarefa.setUsuarioResponsavel(t.getUsuarioResponsavel().getNome());
            if(t.getCreateAt() != null)
                tarefa.setCreateAt(t.getCreateAt().toString());
            dataOfReport.add(tarefa);
        });

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dataOfReport);

        Map<String, Object> params = new HashMap<>();

        params.put("SUBREPORT_DIR", "reportsModels/teste-api-person.jasper");

        params.put("REPORT_LOCALE", Locale.US);
        params.put("One Empty Record", dataOfReport);

        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);

        byte[] reportBytes = JasperExportManager.exportReportToPdf(jasperPrint);

        return ResponseEntity
                .ok()
                .header("Content-Disposition", "inline; filename=relatorio.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(reportBytes);
    }
}
