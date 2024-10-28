import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class CompressedLiterature2Test
{
	private LZWEncoder lzwEncoder;
	@BeforeEach
	public final void setup() throws IOException {
		lzwEncoder = new LZWEncoder();
	}
	
	@Test
	public final void compressed_lit() throws Exception
	{
		assertEquals(true, lzwEncoder.book.book.length() == 3291642 || lzwEncoder.book.book.length() == 3291623, "compressed_lit failed");
		assertEquals(570240, lzwEncoder.book.words.size(), "compressed_lit failed");
		assertEquals(true, lzwEncoder.encodedText.length == 1207161 || lzwEncoder.encodedText.length == 1207173, "compressed_lit failed");
		long size = Files.size(Path.of("./WarAndPeace-compressed.bin"));
		assertEquals(true, size == 1207161 || size == 1207173, "compressed_lit failed");
	}
}


