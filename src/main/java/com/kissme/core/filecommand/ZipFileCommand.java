package com.kissme.core.filecommand;

import static java.io.File.separator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

import com.google.common.collect.Lists;
import com.kissme.lang.IOs;
import com.kissme.lang.Lang;
import com.kissme.lang.file.FileCommand;

/**
 * @author loudyn.
 */
public class ZipFileCommand implements FileCommand {
	private File source;
	private File target;
	private String encoding;

	/**
	 * @param source
	 * @param target
	 */
	public ZipFileCommand(File source, File target) {
		this(source, target, "GBK");
	}

	/**
	 * @param source
	 * @param target
	 * @param encoding
	 */
	public ZipFileCommand(File source, File target, String encoding) {
		this.source = source;
		this.target = target;
		this.encoding = encoding;
	}

	public void execute() {

		List<Throwable> throwables = Lists.newLinkedList();
		ZipArchiveOutputStream zos = null;

		try {

			zos = (ZipArchiveOutputStream) new ArchiveStreamFactory().createArchiveOutputStream("zip", new FileOutputStream(target));
			zos.setEncoding(encoding);
			doInternalArchive(source, zos, "", throwables);

		} catch (Exception e) {
			throwables.add(e);
		} finally {
			IOs.freeQuietly(zos);
		}

		if (!throwables.isEmpty()) {
			throw Lang.comboThrow(throwables);
		}
	}

	private void doInternalArchive(File source, ZipArchiveOutputStream zos, String entryName, List<Throwable> throwables) throws IOException {
		if (source.isDirectory() && !source.isHidden()) {

			zos.putArchiveEntry(new ZipArchiveEntry(entryName + separator));

			for (File child : source.listFiles()) {
				doInternalArchive(child, zos, entryName + separator + child.getName(), throwables);
			}

		}
		else {

			if (source.isHidden()) {
				return;
			}

			InputStream in = null;

			try {

				ZipArchiveEntry entry = new ZipArchiveEntry(entryName);
				zos.putArchiveEntry(entry);

				in = new FileInputStream(source);
				IOs.piping(in, zos);
				zos.closeArchiveEntry();

			} catch (Exception e) {
				throwables.add(e);
			} finally {
				IOs.freeQuietly(in);
			}
		}
	}

}
