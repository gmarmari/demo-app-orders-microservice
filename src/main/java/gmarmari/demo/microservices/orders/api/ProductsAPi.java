package gmarmari.demo.microservices.orders.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/products")
@Tag(name = "Product API", description = "Product management API")
public interface ProductsAPi {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "List of products"
    )
    List<ProductDto> getProducts();

    @GetMapping(path = "/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Get the product with the given product id"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProductDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Product not found")

    })
    ProductDto getProductById(@PathVariable("productId") long productId);

    @GetMapping(path = "/{productId}/details", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Get the details of the product with the given product id"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProductDetailsDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Product not found")

    })
    ProductDetailsDto getProductDetailsById(@PathVariable("productId") long productId);

    @DeleteMapping(path = "/{productId}")
    @Operation(
            description = "Delete the product and its details with the given product id"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Product was deleted"),
            @ApiResponse(
                    responseCode = "500",
                    description = "An error occurred by deleting the product")

    })
    void deleteById(@PathVariable("productId") long productId);

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Save the given product and its details"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Product was saved"),
            @ApiResponse(
                    responseCode = "500",
                    description = "An error occurred by saving the product")

    })
    void saveProduct(@RequestBody ProductDetailsDto productDetails);

}
