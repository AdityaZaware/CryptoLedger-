package com.ben.StockTrading.controller;

import com.ben.StockTrading.entity.Coin;
import com.ben.StockTrading.service.CoinService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("coins")
public class CoinController {

    private final CoinService coinService;
    private final ObjectMapper objectMapper;

    public CoinController(CoinService coinService, ObjectMapper objectMapper) {
        this.coinService = coinService;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public ResponseEntity<List<Coin>> getCoinList(@RequestParam(required = false,
            name = "page" ) int page) throws Exception {

        List<Coin> coins = coinService.getCoinsList(page);
        return new ResponseEntity<>(coins, HttpStatus.ACCEPTED);
    }

    @GetMapping("{coinId}/chart")
    public ResponseEntity<JsonNode> getMarketChart(@PathVariable String coinId,
                                              @RequestParam("days") int days) throws Exception {

        String coins = coinService.getMarketChart(coinId,days);
        JsonNode jsonNode = objectMapper.readTree(coins);

        return new ResponseEntity<>(jsonNode, HttpStatus.ACCEPTED);
    }

    @GetMapping("/serach")
    public ResponseEntity<JsonNode> searchCoin(@RequestParam("q") String keyword) throws Exception {
        String coins = coinService.searchCoin(keyword);
        JsonNode jsonNode = objectMapper.readTree(coins);

        return new ResponseEntity<>(jsonNode, HttpStatus.ACCEPTED);
    }

    @GetMapping("/top50")
    public ResponseEntity<JsonNode> getTop50Coins() throws Exception {
        String coins = coinService.getTop50Coins();
        JsonNode jsonNode = objectMapper.readTree(coins);
        return new ResponseEntity<>(jsonNode, HttpStatus.ACCEPTED);
    }

    @GetMapping("/treading")
    public ResponseEntity<JsonNode> getTreadingCoins() throws Exception {
        String coins = coinService.getTreadingCoins();
        JsonNode jsonNode = objectMapper.readTree(coins);
        return new ResponseEntity<>(jsonNode, HttpStatus.ACCEPTED);
    }

    @GetMapping("details/{coinId}")
    public ResponseEntity<JsonNode> getCoinDetails(@PathVariable String coinId) throws Exception {
        String coins = coinService.getCoinDetails(coinId);
        JsonNode jsonNode = objectMapper.readTree(coins);
        return new ResponseEntity<>(jsonNode, HttpStatus.ACCEPTED);
    }
}
