import java.util.ArrayList;

/* Maximum Fibonacci Heap Implementation */

	public class MaxFibonacciHeap {

	   		int numOfNodes;
	   		DDGNode maxNode;

			/* insert node */
		  public void insert(DDGNode ddgnode) {
				//check if there are no nodes or any number of nodes already present
				 if (numOfNodes == 0) {
					  maxNode = ddgnode;
				 }
				 else {
		 			 ddgnode.prev = maxNode.prev;
		 			 maxNode.prev.next = ddgnode;

					 ddgnode.next = maxNode;
		 			 maxNode.prev = ddgnode;

		 				 if (ddgnode.key > maxNode.key) {
								 maxNode = ddgnode;
						 }
		 		 }
				 // updating total number of nodes
		 		 numOfNodes++;
		  }

			/*  remove the max node */
	    public void removeMax() {

	        DDGNode tempMax = maxNode;
	        // adding children of max to Fibonacci Heap
	        while (tempMax.child != null) {
	            DDGNode child = tempMax.child;

					// remove child
						 DDGNode tempNode = child;
						 tempNode.prev.next = tempNode.next;
						 tempNode.next.prev = tempNode.prev;

	            if (child.next == child) {
								tempMax.child = null;
							}
	            else {
								tempMax.child = child.next;
							}

			        child.prev = maxNode.prev;
			        maxNode.prev.next = child;
			        child.next = maxNode;
			        maxNode.prev = child;
		          child.parent = null;
		        }

						// remove tempMax(maxNode) from child list
						DDGNode tempNodeMax = tempMax;
						tempNodeMax.prev.next = tempNodeMax.next;
						tempNodeMax.next.prev = tempNodeMax.prev;

		        if (tempMax.next == tempMax) {
							maxNode = null;
						}
		        else {
							// perform degree wise Merge
		            maxNode = tempMax.next;
		            degreewiseMerge();
		        }

						tempMax = null;
						// decrease number of nodes, as children of max are reduced
						numOfNodes--;

		    }


		  /* after remove max, we have to perform Degree wise Merge of trees (roots)
			 * if 2 trees have same degree, then merge them
			 */
    		void degreewiseMerge() {
	        // Maximum degree possible --> log2(numOfNodes)
	        int maxDegree = (int) Math.ceil(Math.log(numOfNodes) / Math.log(2.0)) + 1;

					ArrayList<DDGNode> degreeList = new ArrayList<DDGNode>(maxDegree);

        // Finding number of trees (root nodes)
	        int numOfTrees = 0;
	        DDGNode p = maxNode;

            if (p != null) {
                numOfTrees++;
                p = p.next;

                while (p != maxNode) {
                    numOfTrees++;
                    p = p.next;
                }
            }

						// Initialize degree list with null values before using it
						for (int i = 0; i < maxDegree ; i++) {
								degreeList.add(null);
						}

			// combine and merge the nodes which are of same degree (For each node in root list)
	        while (numOfTrees > 0) {

	            int x = p.degree;
							DDGNode next = p.next;

	            // While degree remains the same
	            while (degreeList.get(x) != null) {
	                DDGNode q = degreeList.get(x);    // degree q equals degree p
	                if (p.key < q.key) {              // Check whose key value is greater (key p > key q)
	                    DDGNode temp = q;
	                    q = p;
	                    p = temp;
	                }

	              	// Now join q to p
									DDGNode qnode = q;
									DDGNode pnode = p;

									qnode.next.prev = qnode.prev;
									qnode.prev.next = qnode.next;
									qnode.parent = pnode;

									if (pnode.child == null) {
										pnode.child = qnode;
										qnode.next = qnode;
										qnode.prev = qnode;
									}
									else {
										qnode.prev = pnode.child;
									}

									qnode.next = pnode.child.next;
									pnode.child.next = qnode;
									qnode.next.prev = qnode;

									pnode.degree++;
									qnode.childCut = false;
                // join complete

	                degreeList.set(x, null);
	                x++;
	            }
	            degreeList.set(x, p);
	            p = next;
	            numOfTrees--;
	        }
	        maxNode = null;

	        // add nodes of degreeList to the root
	        for (int i=0; i < maxDegree; i++) {
	        	DDGNode degnode = degreeList.get(i);
	            if (degnode != null) {
	                if (maxNode == null) {
	                    maxNode = degnode;
	                }
	                else {
	                    degnode.prev.next = degnode.next;
	                    degnode.next.prev = degnode.prev;
	                    degnode.prev = maxNode;
	                    degnode.next = maxNode.next;
	                    maxNode.next = degnode;
	                    degnode.next.prev = degnode;

	                    if (degnode.key > maxNode.key) {
												maxNode = degnode;
											}

	                }
	            }
	        }
	    }


	    /* cut node p from q, and join p to the root */
	     void cut(DDGNode p, DDGNode q) {
	        // removes p from child list of q and decrease degree of q
	        p.prev.next = p.next;
	        p.next.prev = p.prev;
	        q.degree--;

	        // if needed, reset q.child
	        if (q.child == p) {
	            q.child = p.next;
	        }

	        if (q.degree == 0) {
	            q.child = null;
	        }

	        // add p to root list (tree list) of heap
	        p.prev = maxNode;
	        p.next = maxNode.next;
	        maxNode.next = p;
	        p.next.prev = p;

	        // set parent of p to null
	        p.parent = null;

	        // set childCut value to false
	        p.childCut = false;
	    }

	    /* Perform cascading cut on the node */
	     void cascadeCut(DDGNode ddgnode) {
	        DDGNode parent = ddgnode.parent;

					//if parent exists
	        if (parent != null) {
						// if parent is false, change it to true
	            if (ddgnode.childCut == false)
	                ddgnode.childCut = true;
	            else {
								// if parent is true, keep performing cascading cut
	                cut(ddgnode, parent);
	                cascadeCut(parent);
	            }
	        }
	    }

	    /* increase the value of key for the given node */
	    public void increaseKey(DDGNode p, int q) {

	        if (maxNode==null || p==null || q < p.key ) {
	            return ;
	        }
					// 'q' is the new increased value
					p.key = q;
          DDGNode newnode = p.parent;

	        if (newnode!=null && (p.key > newnode.key)) {
	     // remove the node from its parent and add it to the root (tree) list
	            cut(p, newnode);
			// after newnode is cut, we perform cascading cut
	            cascadeCut(newnode);
	        }
	        // now update the max node
	        if (p.key > maxNode.key) {
						  maxNode = p;
					}

	    }

			/* returns the maximum key */
			public int fetchMaxKey() {
					if (maxNode != null) {
						return maxNode.key;
					}
					else {
						return -1;
					}
			}

	    /* returns the maximum node of FibnacciHeap */
	    public DDGNode fetchMaxDDGNode() {
	    	return maxNode;
	    }

	}
