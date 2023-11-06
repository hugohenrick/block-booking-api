package com.hugo.blockbookingapi.controller;

import com.hugo.blockbookingapi.model.Block;
import com.hugo.blockbookingapi.service.BlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/blocks")
public class BlockController {

    private final BlockService blockService;

    @Autowired
    public BlockController(BlockService blockService) {
        this.blockService = blockService;
    }

    @PostMapping
    public ResponseEntity<Block> createBlock(@RequestBody Block block) {
        Block newBlock = blockService.createBlock(block);
        return new ResponseEntity<>(newBlock, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Block>> getBlock(@PathVariable Long id) {
        Optional<Block> block = blockService.getBlockById(id);
        return ResponseEntity.ok(block);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Block> updateBlock(@PathVariable Long id, @RequestBody Block block) {
        Block updatedBlock = blockService.updateBlock(id, block);
        return ResponseEntity.ok(updatedBlock);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBlock(@PathVariable Long id) {
        blockService.deleteBlock(id);
        return ResponseEntity.noContent().build();
    }
}


