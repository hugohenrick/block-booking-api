package com.hugo.blockbookingapi.service.impl;

import com.hugo.blockbookingapi.exception.BlockNotFoundException;
import com.hugo.blockbookingapi.model.Block;
import com.hugo.blockbookingapi.model.Booking;
import com.hugo.blockbookingapi.repository.BlockRepository;
import com.hugo.blockbookingapi.repository.BookingRepository;
import com.hugo.blockbookingapi.service.BlockService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BlockServiceImpl implements BlockService {

    private final BlockRepository blockRepository;
    private final BookingRepository bookingRepository;

    @Autowired
    public BlockServiceImpl(BlockRepository blockRepository, BookingRepository bookingRepository) {
        this.blockRepository = blockRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    @Transactional
    public Block createBlock(Block block) {
        List<Booking> overlappingBookings = bookingRepository.findOverlappingBookings(block.getProperty().getId(), block.getStartDate(), block.getEndDate());
        if (!overlappingBookings.isEmpty()) {
            throw new IllegalArgumentException("Block overlaps with existing bookings");
        }
        return blockRepository.save(block);
    }

    @Override
    public Optional<Block> getBlockById(Long id) {
        return Optional.ofNullable(blockRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Block not found with id: " + id)));
    }

    @Override
    @Transactional
    public Block updateBlock(Long id, Block block) {
        return blockRepository.save(block);
    }

    @Override
    @Transactional
    public void deleteBlock(Long id) {
        if (blockRepository.existsById(id)) {
            blockRepository.deleteById(id);
        } else {
            throw new BlockNotFoundException("Block with id " + id + " does not exist");
        }
    }
}

