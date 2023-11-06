package com.hugo.blockbookingapi.service;

import com.hugo.blockbookingapi.model.Block;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public interface BlockService {
    Block createBlock(Block block);

    Optional<Block> getBlockById(Long id);

    Block updateBlock(Long id, Block block);

    void deleteBlock(Long id);
}
