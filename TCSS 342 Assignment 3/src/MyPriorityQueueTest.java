
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// Test provided by professor

public class MyPriorityQueueTest
{
	private MyPriorityQueue<Integer> queue;
	
	@BeforeEach
	public final void setup()
	{
		queue = new MyPriorityQueue<Integer>();
	}
	
	@Test
	public final void insert_0()
	{
		insert_ten_items_asc(queue);
		assertEquals("[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]", queue.toString(), "insert_0 failed");
	}
	
	@Test
	public final void insert_1()
	{
		insert_ten_items_des(queue);
		assertEquals("[0, 1, 4, 3, 2, 8, 5, 9, 6, 7]", queue.toString(), "insert_1 failed");
	}
	
	@Test
	public final void insert_2()
	{
		insert_ten_items_ran(queue);
		assertEquals("[0, 1, 6, 2, 4, 7, 8, 5, 3, 9]", queue.toString(), "insert_2 failed");
	}
	
	@Test
	public final void insert_3()
	{
		insert_ten_items_ran(queue);
		insert_ten_items_ran(queue);
		assertEquals("[0, 0, 1, 1, 4, 6, 3, 5, 2, 9, 5, 7, 7, 8, 4, 6, 8, 3, 2, 9]", queue.toString(), "insert_3 failed");
	}
	
	@Test
	public final void size_0()
	{
		assertEquals(0, queue.size(), "size_0 failed");
	}
	
	@Test
	public final void size_1()
	{
		queue.insert(0);
		assertEquals(1, queue.size(), "size_1 failed");
	}
	
	@Test
	public final void size_2()
	{
		insert_ten_items_ran(queue);
		assertEquals(10, queue.size(), "size_2 failed");
	}
	
	@Test
	public final void size_3()
	{
		insert_ten_items_ran(queue);
		insert_ten_items_ran(queue);
		assertEquals(20, queue.size(), "size_3 failed");
	}
	
	@Test
	public final void empty_and_non_empty()
	{
		assertEquals(true, queue.isEmpty(), "empty failed");
		insert_ten_items_ran(queue);
		insert_ten_items_ran(queue);
		assertEquals(false, queue.isEmpty(), "non_empty failed");
	}
	
	@Test
	public final void min_0()
	{
		assertEquals(null, queue.min(), "min_0 failed");
		queue.insert(5);
		assertEquals(Integer.valueOf(5), queue.min(), "min_0 failed");
		queue.insert(2);
		assertEquals(Integer.valueOf(2), queue.min(), "min_0 failed");
		queue.insert(1);
		assertEquals(Integer.valueOf(1), queue.min(), "min_0 failed");
		queue.insert(3);
		assertEquals(Integer.valueOf(1), queue.min(), "min_0 failed");
	}
	
	@Test
	public final void remove_0()
	{
		assertEquals(null, queue.removeMin(), "remove_0 failed");
		assertEquals("[]", queue.toString(), "remove_0 failed");
		insert_ten_items_ran(queue);
		assertEquals(Integer.valueOf(0), queue.removeMin(), "remove_0 failed");
		assertEquals("[1, 2, 6, 3, 4, 7, 8, 5, 9]", queue.toString(), "remove_0 failed");
		assertEquals(Integer.valueOf(1), queue.removeMin(), "remove_0 failed");
		assertEquals("[2, 3, 6, 5, 4, 7, 8, 9]", queue.toString(), "remove_0 failed");
		assertEquals(Integer.valueOf(2), queue.removeMin(), "remove_0 failed");
		assertEquals("[3, 4, 6, 5, 9, 7, 8]", queue.toString(), "remove_0 failed");
		assertEquals(Integer.valueOf(3), queue.removeMin(), "remove_0 failed");
		assertEquals("[4, 5, 6, 8, 9, 7]", queue.toString(), "remove_0 failed");
		assertEquals(Integer.valueOf(4), queue.removeMin(), "remove_0 failed");
		assertEquals("[5, 7, 6, 8, 9]", queue.toString(), "remove_0 failed");
		assertEquals(Integer.valueOf(5), queue.removeMin(), "remove_0 failed");
		assertEquals("[6, 7, 9, 8]", queue.toString(), "remove_0 failed");
		assertEquals(Integer.valueOf(6), queue.removeMin(), "remove_0 failed");
		assertEquals("[7, 8, 9]", queue.toString(), "remove_0 failed");
		assertEquals(Integer.valueOf(7), queue.removeMin(), "remove_0 failed");
		assertEquals("[8, 9]", queue.toString(), "remove_0 failed");
		assertEquals(Integer.valueOf(8), queue.removeMin(), "remove_0 failed");
		assertEquals("[9]", queue.toString(), "remove_0 failed");
		assertEquals(Integer.valueOf(9), queue.removeMin(), "remove_0 failed");
		assertEquals("[]", queue.toString(), "remove_0 failed");
		assertEquals(null, queue.removeMin(), "remove_0 failed");
		assertEquals("[]", queue.toString(), "remove_0 failed");
	}
	
	@Test
	public final void example()
	{
		assertEquals(null, queue.min(), "example failed");
		assertEquals("[]", queue.toString(), "example failed");
		queue.insert(4);
		assertEquals(Integer.valueOf(4), queue.min(), "example failed");
		assertEquals("[4]", queue.toString(), "example failed");
		queue.insert(7);
		assertEquals(Integer.valueOf(4), queue.min(), "example failed");
		assertEquals("[4, 7]", queue.toString(), "example failed");
		queue.insert(5);
		assertEquals(Integer.valueOf(4), queue.min(), "example failed");
		assertEquals("[4, 7, 5]", queue.toString(), "example failed");
		queue.insert(2);
		assertEquals(Integer.valueOf(2), queue.min(), "example failed");
		assertEquals("[2, 4, 5, 7]", queue.toString(), "example failed");
		queue.insert(3);
		assertEquals(Integer.valueOf(2), queue.min(), "example failed");
		assertEquals("[2, 3, 5, 7, 4]", queue.toString(), "example failed");
		queue.insert(6);
		assertEquals(Integer.valueOf(2), queue.min(), "example failed");
		assertEquals("[2, 3, 5, 7, 4, 6]", queue.toString(), "example failed");
		queue.insert(8);
		assertEquals(Integer.valueOf(2), queue.min(), "example failed");
		assertEquals("[2, 3, 5, 7, 4, 6, 8]", queue.toString(), "example failed");
		queue.insert(9);
		assertEquals(Integer.valueOf(2), queue.min(), "example failed");
		assertEquals("[2, 3, 5, 7, 4, 6, 8, 9]", queue.toString(), "example failed");
		queue.insert(1);
		assertEquals(Integer.valueOf(1), queue.min(), "example failed");
		assertEquals("[1, 2, 5, 3, 4, 6, 8, 9, 7]", queue.toString(), "example failed");
		queue.insert(0);
		assertEquals(Integer.valueOf(0), queue.min(), "example failed");
		assertEquals("[0, 1, 5, 3, 2, 6, 8, 9, 7, 4]", queue.toString(), "example failed");
	}
	
	// ---------- helper methods ---------
	
	public final void insert_ten_items_asc(MyPriorityQueue<Integer> queue)
	{
		for (int index = 0; index < 10; index++)
			queue.insert(index);
	}
	
	public final void insert_ten_items_des(MyPriorityQueue<Integer> queue)
	{
		for (int index = 9; index >= 0; index--)
			queue.insert(index);
	}
	
	public final void insert_ten_items_ran(MyPriorityQueue<Integer> queue)
	{
		int array[] = { 5, 3, 7, 1, 4, 6, 8, 2, 0, 9 }; 
		for (int index = 0; index < array.length; index++)
			queue.insert(array[index]);
	}
}


