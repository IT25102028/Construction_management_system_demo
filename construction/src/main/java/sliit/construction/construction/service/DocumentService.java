package sliit.construction.construction.service;
import sliit.construction.construction.dto.DocumentDtos;
import org.springframework.data.domain.*;
public interface DocumentService {
 DocumentDtos.Response create(DocumentDtos.Request r); Page<DocumentDtos.Response> list(Long projectId,String search,Pageable p);
 DocumentDtos.Response get(Long id); DocumentDtos.Response update(Long id,DocumentDtos.Request r); void delete(Long id);
}
