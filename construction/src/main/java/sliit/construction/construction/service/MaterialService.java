package sliit.construction.construction.service;
import sliit.construction.construction.dto.MaterialDtos;
import sliit.construction.construction.entity.DeliveryStatus;
import org.springframework.data.domain.*;
public interface MaterialService {
 MaterialDtos.Response create(MaterialDtos.Request r); Page<MaterialDtos.Response> list(String search,DeliveryStatus status,Pageable p);
 MaterialDtos.Response get(Long id); MaterialDtos.Response update(Long id,MaterialDtos.Request r); void delete(Long id);
}
