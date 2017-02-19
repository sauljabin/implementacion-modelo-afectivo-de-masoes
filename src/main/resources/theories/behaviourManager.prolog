emotionType(admiration, positive).
emotionType(compassion, positive).
emotionType(happiness, positive).
emotionType(joy, positive).
emotionType(rejection, negative_low).
emotionType(sadness, negative_low).
emotionType(anger, negative_high).
emotionType(depression, negative_high).
behaviourPriority(EMOTION, imitative) :- emotionType(EMOTION, positive).
behaviourPriority(EMOTION, cognitive) :- emotionType(EMOTION, negative_low).
behaviourPriority(EMOTION, reactive) :- emotionType(EMOTION, negative_high).
