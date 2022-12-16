package gmarmari.demo.microservices.orders.api.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import gmarmari.demo.microservices.orders.adapters.OrderAdapter;
import gmarmari.demo.microservices.orders.api.OrderDetailsDto;
import gmarmari.demo.microservices.orders.api.OrderDto;
import gmarmari.demo.microservices.orders.api.ProductDto;
import gmarmari.demo.microservices.orders.api.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Optional;

import static gmarmari.demo.microservices.orders.CommonDataFactory.aLong;
import static gmarmari.demo.microservices.orders.OrderDataFactory.aOrderDetailsDto;
import static gmarmari.demo.microservices.orders.OrderDataFactory.aOrderDto;
import static gmarmari.demo.microservices.orders.ProductDataFactory.aProductDto;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrderRestController.class)
class OrderRestControllerTest {

    private static final String USER_NAME = "super_user";

    @MockBean
    private OrderAdapter adapter;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Test
    void getOrders() throws Exception {
        // Given
        List<OrderDto> list = List.of(aOrderDto(), aOrderDto());
        when(adapter.getOrders(USER_NAME)).thenReturn(list);

        // When
        ResultActions resultActions = mockMvc.perform(get("/orders"));

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(list)));
    }


    @Test
    void getOrderById() throws Exception {
        // Given
        long orderId = aLong();
        OrderDto dto = aOrderDto(orderId);
        when(adapter.getOrder(orderId)).thenReturn(Optional.of(dto));

        // When
        ResultActions resultActions = mockMvc.perform(get("/orders/{id}", orderId));

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(dto)));
    }

    @Test
    void getOrderById_notFound() throws Exception {
        // Given
        long orderId = aLong();
        when(adapter.getOrder(orderId)).thenReturn(Optional.empty());

        // When
        ResultActions resultActions = mockMvc.perform(get("/orders/{orderId}", orderId));

        // Then
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    void getOrderDetailsById() throws Exception {
        // Given
        long orderId = aLong();
        OrderDetailsDto dto = aOrderDetailsDto(orderId);
        when(adapter.getOrderDetails(orderId)).thenReturn(Optional.of(dto));

        // When
        ResultActions resultActions = mockMvc.perform(get("/orders/{orderId}/details", orderId));

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(dto)));
    }

    @Test
    void getOrderDetailsById_notFound() throws Exception {
        // Given
        long orderId = aLong();
        when(adapter.getOrderDetails(orderId)).thenReturn(Optional.empty());

        // When
        ResultActions resultActions = mockMvc.perform(get("/orders/{orderId}/details", orderId));

        // Then
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    void getOrderProducts() throws Exception {
        // Given
        long orderId = aLong();
        List<ProductDto> list = List.of(aProductDto(), aProductDto(), aProductDto());
        when(adapter.getOrderProducts(orderId)).thenReturn(list);

        // When
        ResultActions resultActions = mockMvc.perform(get("/orders/{orderId}/products", orderId));

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(list)));
    }

    @Test
    void deleteById() throws Exception {
        // Given
        long orderId = aLong();
        when(adapter.delete(orderId)).thenReturn(Response.OK);

        // When
        ResultActions resultActions = mockMvc.perform(delete("/orders/{id}", orderId));

        // Then
        resultActions.andExpect(status().isOk());
    }

    @Test
    void deleteById_error() throws Exception {
        // Given
        long orderId = 123;
        when(adapter.delete(orderId)).thenReturn(Response.ERROR);

        // When
        ResultActions resultActions = mockMvc.perform(delete("/orders/{id}", orderId));

        // Then
        resultActions.andExpect(status().isInternalServerError());
    }

    @Test
    void saveOrder() throws Exception {
        // Given
        OrderDetailsDto dto = aOrderDetailsDto();
        when(adapter.save(USER_NAME, dto)).thenReturn(Response.OK);

        // When
        ResultActions resultActions = mockMvc.perform(post("/orders").
                contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto)));

        // Then
        resultActions.andExpect(status().isOk());
    }

    @Test
    void saveOrder_error() throws Exception {
        // Given
        OrderDetailsDto dto = aOrderDetailsDto();
        when(adapter.save(USER_NAME, dto)).thenReturn(Response.ERROR);

        // When
        ResultActions resultActions = mockMvc.perform(post("/orders").
                contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto)));

        // Then
        resultActions.andExpect(status().isInternalServerError());
    }

    @Test
    void saveOrderProducts() throws Exception {
        // Given
        long orderId = aLong();
        List<ProductDto> list = List.of(aProductDto(), aProductDto(), aProductDto());
        when(adapter.saveOrderProducts(orderId, list)).thenReturn(Response.OK);

        // When
        ResultActions resultActions = mockMvc.perform(post("/orders/{orderId}/products", orderId).
                contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(list)));

        // Then
        resultActions.andExpect(status().isOk());
    }

    @Test
    void saveOrderProducts_error() throws Exception {
        // Given
        long orderId = aLong();
        List<ProductDto> list = List.of(aProductDto(), aProductDto(), aProductDto());
        when(adapter.saveOrderProducts(orderId, list)).thenReturn(Response.ERROR);

        // When
        ResultActions resultActions = mockMvc.perform(post("/orders/{orderId}/products", orderId).
                contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(list)));

        // Then
        resultActions.andExpect(status().isInternalServerError());
    }




}