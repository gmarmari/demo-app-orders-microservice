package gmarmari.demo.microservices.orders.adapters;

import gmarmari.demo.microservices.orders.api.ProductOldDto;
import gmarmari.demo.microservices.orders.api.Response;
import gmarmari.demo.microservices.orders.entities.ProductOldDao;
import gmarmari.demo.microservices.orders.services.ProductOldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ProductOldAdapter {

    private final ProductOldService service;

    @Autowired
    public ProductOldAdapter(ProductOldService service) {
        this.service = service;
    }

    public Response save(ProductOldDto productDto) {
        try {
            service.save(convert(productDto));
            return Response.OK;
        } catch (Exception e) {
            return Response.ERROR;
        }
    }

    public Response delete(long productId) {
        try {
            service.delete(productId);
            return Response.OK;
        } catch (Exception e) {
            return Response.ERROR;
        }
    }

    public Optional<ProductOldDto> getProduct(long productId) {
        return service.getProduct(productId).map(ProductOldAdapter::convert);
    }

    public List<ProductOldDto> getProducts() {
        return service.getProducts().stream()
                .map(ProductOldAdapter::convert)
                .collect(Collectors.toList());
    }

    private static ProductOldDao convert(ProductOldDto dto) {
        ProductOldDao dao = new ProductOldDao();
        dao.setId(dto.id);
        dao.setName(dto.name);
        dao.setBrand(dto.brand);
        dao.setDescription(dto.description);
        dao.setColor(dto.color);
        dao.setHeightMm(dto.heightMm);
        dao.setWidthMm(dto.widthMm);
        dao.setLengthMm(dto.lengthMm);
        dao.setWeightGrams(dto.weightGrams);
        dao.setPrizeEuro(dto.prizeEuro);
        return dao;
    }

    private static ProductOldDto convert(ProductOldDao dao) {
        return new ProductOldDto(
                dao.getId(),
                dao.getName(),
                dao.getBrand(),
                dao.getDescription(),
                dao.getColor(),
                dao.getLengthMm(),
                dao.getWidthMm(),
                dao.getHeightMm(),
                dao.getWeightGrams(),
                dao.getPrizeEuro()
        );
    }





}
