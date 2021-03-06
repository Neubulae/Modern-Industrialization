/*
 * MIT License
 *
 * Copyright (c) 2020 Azercoco & Technici4n
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package aztech.modern_industrialization.inventory;

import aztech.modern_industrialization.util.NbtHelper;
import com.google.common.primitives.Ints;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import net.fabricmc.fabric.api.lookup.v1.item.ItemKey;
import net.fabricmc.fabric.api.transfer.v1.item.ItemPreconditions;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.Participant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionResult;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.slot.Slot;

/**
 * An item stack that can be configured.
 */
public class ConfigurableItemStack implements StorageView<ItemKey>, Participant<ItemState> {
    private ItemKey key = ItemKey.EMPTY;
    private int count = 0;
    private Item lockedItem = null;
    private boolean playerLocked = false;
    private boolean machineLocked = false;
    private boolean playerLockable = true;
    private boolean playerInsert = false;
    private boolean playerExtract = true;
    private boolean pipesInsert = false;
    private boolean pipesExtract = false;

    public ConfigurableItemStack() {
    }

    public static ConfigurableItemStack standardInputSlot() {
        ConfigurableItemStack stack = new ConfigurableItemStack();
        stack.playerInsert = true;
        stack.pipesInsert = true;
        return stack;
    }

    public static ConfigurableItemStack standardOutputSlot() {
        ConfigurableItemStack stack = new ConfigurableItemStack();
        stack.pipesExtract = true;
        return stack;
    }

    public static ConfigurableItemStack standardIOSlot(boolean pipeIO) {
        ConfigurableItemStack stack = new ConfigurableItemStack();
        stack.playerInsert = true;
        if (pipeIO) {
            stack.pipesInsert = true;
            stack.pipesExtract = true;
        }
        return stack;
    }

    public ConfigurableItemStack(ConfigurableItemStack other) {
        this();
        this.key = other.key;
        this.count = other.count;
        this.lockedItem = other.lockedItem;
        this.playerLocked = other.playerLocked;
        this.machineLocked = other.machineLocked;
        this.playerLockable = other.playerLockable;
        this.playerInsert = other.playerInsert;
        this.playerExtract = other.playerExtract;
        this.pipesInsert = other.pipesInsert;
        this.pipesExtract = other.pipesExtract;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ConfigurableItemStack that = (ConfigurableItemStack) o;
        return playerLocked == that.playerLocked && machineLocked == that.machineLocked && playerLockable == that.playerLockable
                && playerInsert == that.playerInsert && playerExtract == that.playerExtract && pipesInsert == that.pipesInsert
                && pipesExtract == that.pipesExtract && lockedItem == that.lockedItem && count == that.count && key.equals(that.key);
    }

    /**
     * Create a copy of a list of configurable fluid stacks.
     */
    public static ArrayList<ConfigurableItemStack> copyList(List<ConfigurableItemStack> list) {
        ArrayList<ConfigurableItemStack> copy = new ArrayList<>(list.size());
        for (ConfigurableItemStack stack : list) {
            copy.add(new ConfigurableItemStack(stack));
        }
        return copy;
    }

    public ItemKey getItemKey() {
        return key;
    }

    public int getCount() {
        return count;
    }

    public Item getLockedItem() {
        return lockedItem;
    }

    public void setCount(int count) {
        this.count = count;
        if (count == 0) {
            this.key = ItemKey.EMPTY;
        }
    }

    public void increment(int count) {
        setCount(this.count + count);
    }

    public void decrement(int count) {
        increment(-count);
    }

    public void setItemKey(ItemKey key) {
        this.key = key;
    }

    public boolean isValid(ItemStack stack) {
        return isValid(stack.getItem());
    }

    public boolean isValid(Item item) {
        return lockedItem == null || lockedItem == item;
    }

    public boolean isPlayerLocked() {
        return playerLocked;
    }

    public boolean isMachineLocked() {
        return machineLocked;
    }

    public void enableMachineLock(Item lockedItem) {
        if (this.lockedItem != null && lockedItem != this.lockedItem)
            throw new RuntimeException("Trying to override locked item");
        machineLocked = true;
        this.lockedItem = lockedItem;
    }

    public void disableMachineLock() {
        machineLocked = false;
        onToggleLock();
    }

    public void togglePlayerLock(ItemStack cursorStack) {
        if (playerLockable) {
            if (playerLocked && lockedItem == Items.AIR && !cursorStack.isEmpty()) {
                lockedItem = cursorStack.getItem();
            } else {
                playerLocked = !playerLocked;
            }
            onToggleLock();
        }
    }

    private void onToggleLock() {
        if (!machineLocked && !playerLocked) {
            lockedItem = null;
        } else if (lockedItem == null) {
            lockedItem = key.getItem();
        }
    }

    public boolean canPlayerLock() {
        return playerLockable;
    }

    public CompoundTag writeToTag(CompoundTag tag) {
        tag.put("key", key.toTag());
        tag.putInt("count", count);
        if (lockedItem != null) {
            NbtHelper.putItem(tag, "lockedItem", lockedItem);
        }
        // TODO: more efficient encoding?
        tag.putBoolean("machineLocked", machineLocked);
        tag.putBoolean("playerLocked", playerLocked);
        tag.putBoolean("playerLockable", playerLockable);
        tag.putBoolean("playerInsert", playerInsert);
        tag.putBoolean("playerExtract", playerExtract);
        tag.putBoolean("pipesInsert", pipesInsert);
        tag.putBoolean("pipesExtract", pipesExtract);
        return tag;
    }

    public void readFromTag(CompoundTag tag) {
        // compat
        if (tag.contains("key")) {
            key = ItemKey.fromTag(tag.getCompound("key"));
            count = tag.getInt("count");
        } else {
            ItemStack stack = ItemStack.fromTag(tag);
            key = ItemKey.of(stack);
            count = stack.getCount();
        }
        if (tag.contains("lockedItem")) {
            lockedItem = NbtHelper.getItem(tag, "lockedItem");
        }
        machineLocked = tag.getBoolean("machineLocked");
        playerLocked = tag.getBoolean("playerLocked");
        playerLockable = tag.getBoolean("playerLockable");
        playerInsert = tag.getBoolean("playerInsert");
        playerExtract = tag.getBoolean("playerExtract");
        pipesInsert = tag.getBoolean("pipesInsert");
        pipesExtract = tag.getBoolean("pipesExtract");
    }

    /**
     * Try locking the slot to the given item, return true if it succeeded
     */
    public boolean playerLock(Item item) {
        if ((key.isEmpty() || key.getItem() == item) && (lockedItem == null || lockedItem == Items.AIR)) {
            lockedItem = item;
            playerLocked = true;
            return true;
        }
        return false;
    }

    public boolean canPipesExtract() {
        return pipesExtract;
    }

    public boolean canPipesInsert() {
        return pipesInsert;
    }

    @Override
    public long extract(ItemKey itemKey, long longCount, Transaction transaction) {
        ItemPreconditions.notEmptyNotNegative(key, longCount);
        if (pipesExtract && key.equals(this.key)) {
            int maxCount = Ints.saturatedCast(longCount);
            int extracted = Math.min(count, maxCount);
            transaction.enlist(this);
            decrement(extracted);
            return extracted;
        }
        return 0;
    }

    @Override
    public ItemKey resource() {
        return key;
    }

    @Override
    public long amount() {
        return count;
    }

    @Override
    public ItemState onEnlist() {
        return new ItemState(key, count);
    }

    @Override
    public void onClose(ItemState itemState, TransactionResult result) {
        if (result.wasAborted()) {
            this.count = itemState.count;
            this.key = itemState.key;
        }
    }

    public class ConfigurableItemSlot extends Slot {
        private final Predicate<ItemStack> insertPredicate;
        private final Runnable markDirty;
        // Vanilla MC code modifies the stack returned by `getStack()` directly, but it
        // calls `markDirty()` when that happens, so we just cache the returned stack,
        // and set it when `markDirty()` is called.
        private ItemStack cachedReturnedStack = null;

        public ConfigurableItemSlot(Runnable markDirty, int id, int x, int y, Predicate<ItemStack> insertPredicate) {
            super(null, id, x, y);

            this.insertPredicate = insertPredicate;
            this.markDirty = markDirty;
        }

        @Override
        public boolean canInsert(ItemStack stack) {
            return playerInsert && ConfigurableItemStack.this.isValid(stack) && insertPredicate.test(stack);
        }

        @Override
        public boolean canTakeItems(PlayerEntity playerEntity) {
            return playerExtract;
        }

        public ConfigurableItemStack getConfStack() {
            return ConfigurableItemStack.this;
        }

        @Override
        public ItemStack getStack() {
            return cachedReturnedStack = key.toStack(count);
        }

        @Override
        public void setStack(ItemStack stack) {
            key = ItemKey.of(stack);
            count = stack.getCount();
            markDirty.run();
            cachedReturnedStack = stack;
        }

        @Override
        public void markDirty() {
            if (cachedReturnedStack != null) {
                setStack(cachedReturnedStack);
            }
        }

        @Override
        public int getMaxItemCount() {
            return 64;
        }

        @Override
        public ItemStack takeStack(int amount) {
            ItemStack stack = key.toStack(amount);
            decrement(amount);
            cachedReturnedStack = null;
            markDirty.run();
            return stack;
        }
    }
}
