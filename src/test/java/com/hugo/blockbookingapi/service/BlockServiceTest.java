package com.hugo.blockbookingapi.service;

import com.hugo.blockbookingapi.model.Block;
import com.hugo.blockbookingapi.model.Property;
import com.hugo.blockbookingapi.repository.BlockRepository;
import com.hugo.blockbookingapi.repository.BookingRepository;
import com.hugo.blockbookingapi.service.impl.BlockServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
class BlockServiceTest {

    @Mock
    private BlockRepository blockRepository;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BlockServiceImpl blockService;

    @Test
    void createBlockTest() {
        Block block = new Block();
        block.setId(1L);
        block.setStartDate(LocalDate.of(2023, 11, 10));
        block.setEndDate(LocalDate.of(2023, 11, 15));
        Property property = new Property();
        property.setId(1L);
        property.setName("Beach House");
        block.setProperty(property);

        when(bookingRepository.findOverlappingBookings(
                eq(property.getId()),
                eq(block.getStartDate()),
                eq(block.getEndDate())))
                .thenReturn(Collections.emptyList());

        when(blockRepository.save(any(Block.class))).thenReturn(block);

        Block createdBlock = blockService.createBlock(block);

        assertThat(createdBlock).isNotNull();
        assertThat(createdBlock.getId()).isEqualTo(block.getId());
        assertThat(createdBlock.getStartDate()).isEqualTo(block.getStartDate());
        assertThat(createdBlock.getEndDate()).isEqualTo(block.getEndDate());

        verify(blockRepository).save(block);
    }


    @Test
    void updateBlockTest() {
        Long blockId = 1L;
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(1);

        Property property = new Property();
        property.setId(1L);
        property.setName("Beach House");

        Block existingBlock = new Block();
        existingBlock.setId(blockId);
        existingBlock.setStartDate(startDate);
        existingBlock.setEndDate(endDate);
        existingBlock.setProperty(property);

        Block updatedDetails = new Block();
        updatedDetails.setStartDate(startDate.plusDays(3));
        updatedDetails.setEndDate(endDate.plusDays(3));
        updatedDetails.setProperty(property);

        when(blockRepository.findById(blockId)).thenReturn(Optional.of(existingBlock));
        when(blockRepository.findAllByPropertyAndPeriod(anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Collections.emptyList()); // Simulate that there are no overlapping blocks
        when(blockRepository.save(any(Block.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Block updatedBlock = blockService.updateBlock(blockId, updatedDetails);

        assertThat(updatedBlock).isNotNull();
        assertThat(updatedBlock.getId()).isEqualTo(existingBlock.getId());
        assertThat(updatedBlock.getStartDate()).isEqualTo(updatedDetails.getStartDate());
        assertThat(updatedBlock.getEndDate()).isEqualTo(updatedDetails.getEndDate());
    }

    @Test
    void deleteBlockTest() {
        Long blockId = 1L;

        blockService.deleteBlock(blockId);

        verify(blockRepository).deleteById(blockId);
    }

}

