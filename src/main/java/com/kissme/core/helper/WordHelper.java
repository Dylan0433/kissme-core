package com.kissme.core.helper;

import static java.util.Collections.sort;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import com.kissme.lang.Lang;

/**
 * 
 * @author loudyn
 * 
 */
public class WordHelper {
	
	private WordNode rootWordNode = new WordNode(null, 'R', WordNode.MIDSIDE_TYPE);

	/**
	 * 
	 * @param word
	 * @return
	 */
	public final WordHelper add(String word) {

		char[] chars = word.toCharArray();
		if (chars.length > 0) {
			insertWordNode(rootWordNode, chars, 0);
		}

		return this;
	}

	/**
	 * 
	 * @param word
	 * @return
	 */
	public final WordHelper remove(String word) {

		char[] chars = word.toCharArray();
		if (chars.length > 0) {
			removeWordNode(rootWordNode, chars, 0);
		}

		return this;
	}

	/**
	 * 
	 * @param wordNode
	 * @param chars
	 * @param index
	 */
	private void removeWordNode(WordNode wordNode, char[] chars, int index) {

		convertEnglishAlphabetToLowerCase(chars, index);
		WordNode node = searchWordNode(wordNode, chars[index]);

		if (null == node) {
			return;
		}

		if (index == chars.length - 1) {
			node.type = WordNode.MIDSIDE_TYPE;
		}

		if (++index < chars.length) {
			removeWordNode(node, chars, index);
		}
	}

	/**
	 * 
	 * @param words
	 * @return
	 */
	public final WordHelper addAll(Collection<String> words) {
		for (String word : words) {
			add(word);
		}
		return this;
	}

	/**
	 * 
	 * @param words
	 * @return
	 */
	public final WordHelper removeAll(Collection<String> words) {
		for (String word : words) {
			remove(word);
		}
		return this;
	}

	/**
	 * 
	 * @return
	 */
	public final WordHelper reset() {

		destroyFilterWordTree(rootWordNode);
		rootWordNode = new WordNode(null, 'R', WordNode.MIDSIDE_TYPE);
		return this;
	}

	/**
	 * 
	 * @param wordnode
	 * @return
	 */
	private WordHelper destroyFilterWordTree(WordNode wordnode) {

		if (null != wordnode.children && !wordnode.children.isEmpty()) {
			for (WordNode node : wordnode.children) {
				destroyFilterWordTree(node);
			}
		}
		wordnode.children = null;
		wordnode = null;

		return this;
	}

	/**
	 * 
	 * @param text
	 * @param replacement
	 * @return
	 */
	public final String filter(String text, String replacement) {

		List<String> hitWords = new LinkedList<String>();

		try {
			if (beforeFilter(text, replacement)) {

				hitWords = hit(text);
				return doInternalFilter(text, hitWords, replacement);
			}

		} catch (Exception e) {
			throw Lang.uncheck(e);
		} finally {
			afterFilter(hitWords, text, replacement);
		}

		return text;
	}

	/**
	 * 
	 * @param fatherNode
	 * @param chars
	 * @param index
	 */
	private void insertWordNode(WordNode fatherNode, char[] chars, int index) {

		convertEnglishAlphabetToLowerCase(chars, index);
		WordNode node = searchWordNode(fatherNode, chars[index]);

		if (null == node) {
			node = new WordNode(fatherNode, chars[index], WordNode.MIDSIDE_TYPE);
			fatherNode.children.add(node);
		}

		if (index == chars.length - 1) {
			node.type = node.children.isEmpty() ? WordNode.END_TYPE : WordNode.BOTH_TYPE;
		}

		if (node.parent.type == WordNode.END_TYPE) {
			node.parent.type = WordNode.BOTH_TYPE;
		}

		if (++index < chars.length) {
			insertWordNode(node, chars, index);
		}
	}

	/**
	 * 
	 * @param chars
	 * @param index
	 */
	private void convertEnglishAlphabetToLowerCase(char[] chars, int index) {

		if (chars[index] >= 65 && chars[index] <= 90) {
			chars[index] = (char) (chars[index] + 32);
		}
	}

	/**
	 * 
	 * @param wordNode
	 * @param c
	 * @return
	 */
	private WordNode searchWordNode(WordNode wordNode, char c) {
		List<WordNode> children = wordNode.children;

		for (WordNode node : children) {
			if (node.value == c) {
				return node;
			}
		}

		return null;
	}

	/**
	 * 
	 * @param text
	 * @param replacement
	 * @return
	 */
	protected boolean beforeFilter(String text, String replacement) {
		// add hook by overwrite this method;
		return true;
	}

	/**
	 * 
	 * @param text
	 * @return
	 */
	private List<String> hit(String text) {

		List<String> hitWords = new LinkedList<String>();
		List<Character> foundChars = new LinkedList<Character>();

		WordNode node = rootWordNode;

		int index = 0;
		char[] chars = text.toCharArray();

		while (index < chars.length) {
			convertEnglishAlphabetToLowerCase(chars, index);
			node = searchWordNode(node, chars[index]);

			if (null == node) {
				node = rootWordNode;
				index -= foundChars.size();
				foundChars.clear();
			}

			else if (node.type == WordNode.END_TYPE) {
				node = rootWordNode;
				foundChars.add(chars[index]);
				hitWords.add(charListToString(foundChars));
				index -= (foundChars.size() - 1);
				foundChars.clear();
			}

			else if (node.type == WordNode.BOTH_TYPE) {
				foundChars.add(chars[index]);
				hitWords.add(charListToString(foundChars));
			}

			else {
				foundChars.add(chars[index]);
			}

			index++;
		}

		return hitWords;
	}

	/**
	 * 
	 * @param hitWords
	 * @param text
	 * @param replacement
	 */
	protected void afterFilter(List<String> hitWords, String text, String replacement) {
		// add hook by overwrite this method;
	}

	/**
	 * 
	 * @param chars
	 * @return
	 */
	private String charListToString(List<Character> chars) {
		StringBuilder buf = new StringBuilder();
		for (char c : chars) {
			buf.append(c);
		}
		return buf.toString();
	}

	/**
	 * 
	 * @param text
	 * @param hitWords
	 * @param replacement
	 * @return
	 */
	private String doInternalFilter(String text, List<String> hitWords, String replacement) {

		List<String> copy = new ArrayList<String>(new HashSet<String>(hitWords));
		hitWords.clear();
		hitWords.addAll(copy);

		sort(hitWords, WORD_COMPARATOR);

		for (String foundWord : hitWords) {
			text = text.replaceAll("(?iu)" + PatternHelper.quoteReplace(foundWord), replacement);
		}
		return text;
	}

	private final static Comparator<String> WORD_COMPARATOR = new Comparator<String>() {

		public int compare(String one, String other) {
			return other.length() - one.length();
		}
	};

	/**
	 * 
	 * @author loudyn
	 * 
	 */
	protected static final class WordNode {
		static final int END_TYPE = 1;
		static final int MIDSIDE_TYPE = 1 << 1;
		static final int BOTH_TYPE = END_TYPE | MIDSIDE_TYPE;

		final char value;
		int type;

		WordNode parent;
		List<WordNode> children = new ArrayList<WordNode>(0);

		WordNode(WordNode parent, char value, int type) {
			this.parent = parent;
			this.value = value;
			this.type = type;
		}
	}
}
