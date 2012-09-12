package com.kissme.core.filecommand;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.io.FileUtils;

import com.google.common.collect.Lists;
import com.kissme.lang.IOs;
import com.kissme.lang.Lang;
import com.kissme.lang.file.FileCommand;

/**
 * 
 * @author loudyn
 * 
 */
public class UnzipFileCommand implements FileCommand {

	private final File target;
	private final File source;
	private final String encoding;

	/**
	 * 
	 * @param source
	 * @param target
	 */
	public UnzipFileCommand(File source, File target) {
		this(source, target, "GBK");
	}

	/**
	 * 
	 * @param source
	 * @param target
	 * @param encoding
	 */
	public UnzipFileCommand(File source, File target, String encoding) {
		this.source = source;
		this.target = target;
		this.encoding = encoding;
	}

	@Override
	public void execute() {
		doInternalArchive(source, target, encoding);
	}

	private void doInternalArchive(File source, File target, String encoding) {

		List<Throwable> throwables = Lists.newLinkedList();
		try {

			ZipFile sourceAsZip = new ZipFile(source, encoding);

			for (Enumeration<ZipArchiveEntry> entries = sourceAsZip.getEntries(); entries.hasMoreElements();) {

				ZipArchiveEntry entry = entries.nextElement();
				File entryfile = new File(target, entry.getName());
				
				if (entry.isDirectory()) {
					FileUtils.forceMkdir(entryfile);
					continue;
				}

				InputStream in = null;
				OutputStream out = null;

				try {

					in = sourceAsZip.getInputStream(entry);
					out = new FileOutputStream(entryfile);
					IOs.piping(in, out);

				} catch (Exception e) {
					throwables.add(e);
				} finally {
					IOs.freeQuietly(in, out);
				}
			}

		} catch (Exception e) {
			throwables.add(e);
		}

		if (!throwables.isEmpty()) {
			throw Lang.comboThrow(throwables);
		}

	}

}
