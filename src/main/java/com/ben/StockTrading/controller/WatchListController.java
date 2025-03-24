package com.ben.StockTrading.controller;

import com.ben.StockTrading.entity.Coin;
import com.ben.StockTrading.entity.User;
import com.ben.StockTrading.entity.WatchList;
import com.ben.StockTrading.service.CoinService;
import com.ben.StockTrading.service.UserService;
import com.ben.StockTrading.service.WatchListService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/watchlist")
public class WatchListController {

    private final WatchListService watchListService;
    private final UserService userService;
    private final CoinService coinService;

    public WatchListController(WatchListService watchListService, UserService userService, CoinService coinService) {
        this.watchListService = watchListService;
        this.userService = userService;
        this.coinService = coinService;
    }

    @GetMapping("/user")
    public ResponseEntity<WatchList> getUserWatchList(
            @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);
        WatchList watchList = watchListService.findUserWatchList(user.getId());

        return new ResponseEntity<>(watchList, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<WatchList> createWatchList(
            @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        WatchList watchList = watchListService.createWatchList(user);
        return new ResponseEntity<>(watchList, HttpStatus.OK);
    }

    @GetMapping("/{watchlistId}")
    public ResponseEntity<WatchList> getWatchlistById(
            @PathVariable Long watchlistId) throws Exception {

        WatchList watchList = watchListService.findById(watchlistId);

        return new ResponseEntity<>(watchList, HttpStatus.OK);
    }

    @PatchMapping("/add/{coinId}")
    public ResponseEntity<Coin> addItemToWatchList(
            @RequestHeader("Authorization") String jwt,
            @PathVariable String coinId) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);
        Coin coin = coinService.findById(coinId);
        Coin addedCoin = watchListService.addItemToWatchLis(coin, user);

        return new ResponseEntity<>(addedCoin, HttpStatus.OK);
    }

}
