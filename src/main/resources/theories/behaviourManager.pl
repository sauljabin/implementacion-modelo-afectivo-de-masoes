emotionType(happiness, positive).
emotionType(joy, positive).
emotionType(compassion, positive).
emotionType(admiration, positive).
emotionType(sadness, negative_low).
emotionType(rejection, negative_low).
emotionType(depression, negative_high).
emotionType(anger, negative_high).
behaviourPriority(EMOTION, imitative) :- emotionType(EMOTION, positive).
behaviourPriority(EMOTION, cognitive) :- emotionType(EMOTION, negative_low).
behaviourPriority(EMOTION, reactive) :- emotionType(EMOTION, negative_high).