package sliit.construction.construction.service;
import sliit.construction.construction.dto.MaterialDtos; import sliit.construction.construction.entity.*; import sliit.construction.construction.repository.*; import sliit.construction.construction.exception.*;
import org.springframework.data.domain.*; import org.springframework.stereotype.Service;
@Service public class MaterialServiceImpl implements MaterialService {
 private final MaterialRepository repo; public MaterialServiceImpl(MaterialRepository r){repo=r;}
 public MaterialDtos.Response create(MaterialDtos.Request r){return map(repo.save(build(new Material(),r)));}
 public Page<MaterialDtos.Response> list(String s,DeliveryStatus status,Pageable p){Page<Material> x=s!=null&&!s.isBlank()?repo.findByNameContainingIgnoreCase(s,p):(status!=null?repo.findByDeliveryStatus(status,p):repo.findAll(p));return x.map(this::map);}
 public MaterialDtos.Response get(Long id){return map(entity(id));}
 public MaterialDtos.Response update(Long id,MaterialDtos.Request r){return map(repo.save(build(entity(id),r)));}
 public void delete(Long id){repo.delete(entity(id));}
 private Material entity(Long id){return repo.findById(id).orElseThrow(()->new ResourceNotFoundException("Material not found: "+id));}
 private Material build(Material x,MaterialDtos.Request r){x.setName(r.name());x.setQuantity(r.quantity());x.setUnit(r.unit());x.setSupplier(r.supplier());x.setStockThreshold(r.stockThreshold());x.setPurchaseRequest(r.purchaseRequest());x.setDeliveryStatus(r.deliveryStatus());return x;}
 private MaterialDtos.Response map(Material x){return new MaterialDtos.Response(x.getId(),x.getName(),x.getQuantity(),x.getUnit(),x.getSupplier(),x.getStockThreshold(),x.getPurchaseRequest(),x.getDeliveryStatus(),x.getCreatedAt(),x.getUpdatedAt());}
}
