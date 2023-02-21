(ns mailclienttools.reagent-hooks
  (:require [reagent.core :as r]))


(defn use-state [value]
  (let [r (r/atom value)]
    [r #(reset! r %)]))

(defn use-ref []
  (let [a (volatile! nil)]
    (reify
      IDeref
      (-deref [_]
        @a)
      IFn
      (-invoke [this value]
        (vreset! a value)))))

(defn use-effect [f]
  (let [current-component ^js (r/current-component)
        did-mount (.-componentDidMount current-component)
        will-unmount (.-componentWillUnmount current-component)
        handler (volatile! nil)]

    (set! (.-componentDidMount current-component)
          (fn [& args]
            (let [h (f)]
              (when (fn? h)
                (vreset! handler h)))
            (when (fn? did-mount)
              (apply did-mount args))))

    (set! (.-componentWillUnmount current-component)
          (fn [& args]
            (when (fn? @handler) (@handler))
            (when (fn? will-unmount)
              (apply will-unmount args))))))

(defn use-reducer [reducer initial-state]
  (let [val (r/atom initial-state)]
    [val #(swap! val reducer %)]))