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

@RequestMapping("/orders")
@Tag(name = "Order API", description = "Order management API")
public interface OrdersApi {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "List of orders for the current user"
    )
    List<OrderDto> getOrders();

    @GetMapping(path = "/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Get the order with the given order id"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = OrderDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Order not found")

    })
    OrderDto getOrderById(@PathVariable("orderId") long orderId);

    @GetMapping(path = "/{orderId}/details", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Get the details of the order with the given order id"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = OrderDetailsDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Order not found")

    })
    OrderDetailsDto getOrderDetailsById(@PathVariable("orderId") long orderId);

    @GetMapping(path = "/{orderId}/products", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Get the product ids of the order with the given order id"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = OrderProductDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Order not found")

    })
    List<OrderProductDto> getOrderProductIds(@PathVariable("orderId") long orderId);

    @DeleteMapping(path = "/{orderId}")
    @Operation(
            description = "Delete the order and its details with the given order id"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Order was deleted"),
            @ApiResponse(
                    responseCode = "500",
                    description = "An error occurred by deleting the order")

    })
    void deleteById(@PathVariable("orderId") long orderId);


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Save the given order and its details"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Order was saved"),
            @ApiResponse(
                    responseCode = "500",
                    description = "An error occurred by saving the order")

    })
    void saveOrder(@RequestBody OrderDetailsDto orderDetails);

    @PostMapping(path = "/{orderId}/products", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            description = "Save the ids and amount of the products of the order with the given order id"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Order products were saved"),
            @ApiResponse(
                    responseCode = "500",
                    description = "An error occurred by saving the products of the order")

    })
    void saveOrderProducts(@PathVariable("orderId") long orderId, @RequestBody List<OrderProductDto> products);

}
