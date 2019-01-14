/*
Name : Shivaditya Jatar
UFID: 6203-9241
email: shivadityajatar@ufl.edu
*/
        public class DDGNode {

                  /* DuckDuckGo(DDG) Node Structure */

              DDGNode parent, child;            // parent and child
              DDGNode prev, next;             // prev(left) and next(right) sibling

              int key, degree;                // for storing key and degree

              boolean childCut;           // Child Cut value
              String keyword;               // keyword

              /* Initializing values for DDGNode attributes */
              public DDGNode(int key, String keyword) {
                    this.parent = null;
                    this.child  = null;
                    this.prev   = this;
                    this.next  = this;
                    this.key    = key;
                    this.degree = 0;
                    this.childCut = false;
                    this.keyword = keyword;
              }
        }
