emotion(AGENT, ACTION, compassion) :- satisfaction(AGENT, ACTION, positive_high), other(AGENT).
emotion(AGENT, ACTION, admiration) :- satisfaction(AGENT, ACTION, positive_low), other(AGENT).
emotion(AGENT, ACTION, rejection) :- satisfaction(AGENT, ACTION, negative_low), other(AGENT).
emotion(AGENT, ACTION, anger) :- satisfaction(AGENT, ACTION, negative_high), other(AGENT).
emotion(AGENT, ACTION, happiness) :- satisfaction(AGENT, ACTION, positive_high), self(AGENT).
emotion(AGENT, ACTION, joy) :- satisfaction(AGENT, ACTION, positive_low), self(AGENT).
emotion(AGENT, ACTION, sadness) :- satisfaction(AGENT, ACTION, negative_low), self(AGENT).
emotion(AGENT, ACTION, depression) :- satisfaction(AGENT, ACTION, negative_high), self(AGENT).