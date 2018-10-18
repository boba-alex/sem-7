package Lab1.kasiski;

public class KeyLength {

	private final Integer length;
	private Integer occurrence;

	public KeyLength(final Integer length, final Integer occurrence) {

		this.length = length;
		this.occurrence = occurrence;
	}

	public int getLength() {

		return length;
	}

	public int getOccurrence() {

		return occurrence;
	}

	public void setOccurrence(final Integer occurrence) {

		this.occurrence = occurrence;
	}

	@Override
	public boolean equals(final Object other) {

		if (this == other)
			return true;
		if (other == null || getClass() != other.getClass())
			return false;
		final KeyLength keyLength = (KeyLength) other;
		return length.equals(keyLength.length) && occurrence.equals(keyLength.occurrence);
	}

	@Override
	public String toString() {

		return "length : " + length + " occurrence : " + occurrence;
	}
}
