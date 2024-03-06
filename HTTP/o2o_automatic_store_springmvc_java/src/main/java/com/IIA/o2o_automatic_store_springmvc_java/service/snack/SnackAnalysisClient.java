package com.IIA.o2o_automatic_store_springmvc_java.service.snack;

import com.IIA.o2o_automatic_store_springmvc_java.dto.snack.SnackDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SnackAnalysisClient {
    List<SnackDto> analyzeSnacks(List<MultipartFile> images);
}