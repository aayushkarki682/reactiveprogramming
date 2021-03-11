package com.reactiveprogramming.controllertests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@RunWith(SpringRunner.class)
@WebFluxTest //it is going to scan for annotatted classes like restcontroller, controller and more
             //but it is not going to scan the class annotatted with @component or @service or @repository classes
public class FluxAndMonoControllerTest {

    @Autowired
    WebTestClient webTestClient;  //non-blocking client and it is going to create by the WebFluxTest

    @Test
    public void flux_approach1(){

        Flux<Integer> integerFlux = webTestClient.get().uri("/flux")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()  //this is the call which is going to invoke the endpoint
                .expectStatus().isOk()
                .returnResult(Integer.class)
                .getResponseBody();   //this gives us the actual flux

        StepVerifier.create(integerFlux)
                .expectSubscription()  //we expect the subscription to be send to us
                .expectNext(1)
                .expectNext(2)
                .expectNext(3)
                .expectNext(4)
                .verifyComplete();
    }

    @Test
    public void flux_approach2(){

       webTestClient.get().uri("/flux")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Integer.class)
                .hasSize(4);


    }

    @Test
    public void flux_approach3(){

        List<Integer> integers = Arrays.asList(1,2,3,4);

        EntityExchangeResult<List<Integer>> entityExchangeResult =  webTestClient.get().uri("/flux")
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .exchange()
                        .expectStatus().isOk()
                        .expectBodyList(Integer.class)
                        .returnResult();

        assertEquals(integers, entityExchangeResult.getResponseBody());
    }

    @Test
    public void flux_approach4(){

        List<Integer> integers = Arrays.asList(1,2,3,4);

         webTestClient.get().uri("/flux")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Integer.class)
                .consumeWith((response) -> {
                    assertEquals(
                            integers, response.getResponseBody()
                    );
                });

    }

    @Test
    public void infiniteStreamFluxTest(){

        Flux<Long> longStream = webTestClient.get().uri("/fluxstream")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Long.class)
                .getResponseBody();

        StepVerifier.create(longStream)
                .expectSubscription()
                .expectNext(0l)
                .expectNext(1l)
                .thenCancel()
                .verify();

    }

    @Test
    public void monoTest(){

        Integer monoInteger = new Integer(1);

         webTestClient.get().uri("/mono")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Integer.class)
                .consumeWith((response) -> {
                    assertEquals(monoInteger, response.getResponseBody());
                });
    }
}
