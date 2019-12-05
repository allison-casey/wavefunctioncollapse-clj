(defproject wavefunctioncollapse "0.1.5"
  :description "Bitmap & tilemap generation from a single example with the help of ideas from quantum mechanics."
  :url "https://github.com/sjcasey21/wavefunctioncollapse-clj"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [com.github.sjcasey21/wavefunctioncollapse "0.2.1"]]
  :plugins [[lein-codox "0.10.7"]]
  :profiles {:dev {:dependencies [[cheshire "5.9.0"]
                                  [net.mikera/imagez "0.12.0"]]}}
  :repl-options {:init-ns wavefunctioncollapse.core})
